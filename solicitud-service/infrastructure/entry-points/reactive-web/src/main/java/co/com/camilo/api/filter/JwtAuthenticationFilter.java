package co.com.camilo.api.filter;

import co.com.camilo.model.autenticacion.gateways.JwtService;
import co.com.camilo.model.exceptions.TokenInvalidoException;
import co.com.camilo.model.exceptions.AutenticacionException;

import java.util.List;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }
        System.out.println(">> isPublicEndpoint");
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.print(">> Header {}");
                System.out.println(authHeader);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Token de autorización no encontrado o formato inválido para path: {}", path);
            return Mono.error(new AutenticacionException("Token de autorización requerido"));
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        System.out.print(">> Token: ");
        System.out.println(token);

        return jwtService.validarToken(token)
                .flatMap(usuario -> {
                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + usuario.getNombreRol().toUpperCase())
                    );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(usuario, null, authorities);

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                .onErrorMap(TokenInvalidoException.class, ex -> {
                    log.warn("Token inválido para path: {}", path, ex);
                    return new TokenInvalidoException("Token inválido o expirado");
                })
                .onErrorMap(ex -> {
                    log.error("Error en autenticación para path: {}", path, ex);
                    return new AutenticacionException("Error en el proceso de autenticación", ex);
                });
    }
    
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/actuator") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/api-docs") ||
                path.startsWith("/webjars") ||
                path.equals("/swagger-ui.html");
    }
}