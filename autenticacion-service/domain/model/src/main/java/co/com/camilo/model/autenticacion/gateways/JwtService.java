package co.com.camilo.model.autenticacion.gateways;

import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import reactor.core.publisher.Mono;

public interface JwtService {

    Mono<String> generarToken(UsuarioAutenticado usuario);
    Mono<UsuarioAutenticado> validarToken(String token);
    Mono<Boolean> esTokenValido(String token);

}

