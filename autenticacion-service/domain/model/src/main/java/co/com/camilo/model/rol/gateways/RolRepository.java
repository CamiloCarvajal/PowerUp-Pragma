package co.com.camilo.model.rol.gateways;

import co.com.camilo.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {

    Mono<Rol> findById(int id);
    Mono<Rol> save(Rol rol);

}

