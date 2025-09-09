package co.com.camilo.jwt;

import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import co.com.camilo.model.autenticacion.gateways.JwtService;
import co.com.camilo.model.exceptions.TokenInvalidoException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtServiceAdapter implements JwtService {

    @Value("${jwt.secret:miClaveSecretaMuySeguraParaJWTQueDebeSerMuyLarga}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<String> generarToken(UsuarioAutenticado usuario) {
        return Mono.fromCallable(() -> {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", usuario.getId());
            claims.put("nombre", usuario.getNombre());
            claims.put("apellido", usuario.getApellido());
            claims.put("correoElectronico", usuario.getCorreoElectronico());
            claims.put("idRol", usuario.getIdRol());
            claims.put("nombreRol", usuario.getNombreRol());

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(usuario.getCorreoElectronico())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }).doOnNext(token -> log.debug("Token generado para usuario: {}", usuario.getCorreoElectronico()))
          .onErrorMap(e -> new TokenInvalidoException("Error al generar token: " + e.getMessage(), e));
    }

    @Override
    public Mono<UsuarioAutenticado> validarToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getPayload();

                return UsuarioAutenticado.builder()
                        .id(claims.get("id", Integer.class))
                        .nombre(claims.get("nombre", String.class))
                        .apellido(claims.get("apellido", String.class))
                        .correoElectronico(claims.get("correoElectronico", String.class))
                        .idRol(claims.get("idRol", Integer.class))
                        .nombreRol(claims.get("nombreRol", String.class))
                        .build();

            } catch (JwtException | IllegalArgumentException e) {
                throw new TokenInvalidoException("Token inválido: " + e.getMessage(), e);
            }
        }).doOnNext(usuario -> log.debug("Token validado para usuario: {}", usuario.getCorreoElectronico()))
          .onErrorMap(e -> e instanceof TokenInvalidoException ? e : 
              new TokenInvalidoException("Error al validar token: " + e.getMessage(), e));
    }

    @Override
    public Mono<Boolean> esTokenValido(String token) {
        return validarToken(token)
                .map(usuario -> true)
                .onErrorReturn(false);
    }
}

