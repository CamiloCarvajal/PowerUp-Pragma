package co.com.camilo.usecase.autenticacion;

import co.com.camilo.model.autenticacion.LoginRequest;
import co.com.camilo.model.autenticacion.TokenResponse;
import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import co.com.camilo.model.autenticacion.gateways.AutenticacionRepository;
import co.com.camilo.model.autenticacion.gateways.JwtService;
import co.com.camilo.model.exceptions.AutenticacionException;
import co.com.camilo.model.exceptions.CredencialesInvalidasException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticacionUseCase {

    private final AutenticacionRepository autenticacionRepository;
    private final JwtService jwtService;

    public Mono<TokenResponse> autenticarUsuario(LoginRequest loginRequest) {

        if (loginRequest.getCorreoElectronico() == null || loginRequest.getPassword() == null) {
            return Mono.error(new CredencialesInvalidasException("Correo electrónico y contraseña son requeridos"));
        }

        return autenticacionRepository.autenticarUsuario(loginRequest.getCorreoElectronico(), loginRequest.getPassword())
                .flatMap(usuario -> jwtService.generarToken(usuario)
                        .map(token -> TokenResponse.builder()
                                .token(token)
                                .tipoToken("Bearer")
                                .tiempoExpiracion(86400000L) // 24 horas
                                .usuario(TokenResponse.UsuarioAutenticado.builder()
                                        .id(usuario.getId())
                                        .nombre(usuario.getNombre())
                                        .apellido(usuario.getApellido())
                                        .correoElectronico(usuario.getCorreoElectronico())
                                        .idRol(usuario.getIdRol())
                                        .nombreRol(usuario.getNombreRol())
                                        .build())
                                .build()))
                .doOnError(error -> new AutenticacionException("No se pudo autenticar", error.getCause()));
    }

    public Mono<UsuarioAutenticado> validarToken(String token) {
        return jwtService.validarToken(token)
                .doOnError(exception ->System.out.print("Validación de token: " + exception.getMessage() + " - " + exception.getCause() ));
    }

    public Mono<Boolean> esTokenValido(String token) {
        return jwtService.esTokenValido(token)
                .doOnError(exception ->System.out.print("esTokenValido: " + exception.getMessage() + " - " + exception.getCause() ));
    }
}

