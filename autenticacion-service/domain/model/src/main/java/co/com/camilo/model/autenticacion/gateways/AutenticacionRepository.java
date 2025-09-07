package co.com.camilo.model.autenticacion.gateways;

import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import reactor.core.publisher.Mono;

public interface AutenticacionRepository {

    Mono<UsuarioAutenticado> autenticarUsuario(String correoElectronico, String password);

}

