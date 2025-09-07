package co.com.camilo.r2dbc.repository;

import co.com.camilo.model.rol.Rol;
import co.com.camilo.model.rol.gateways.RolRepository;
import co.com.camilo.r2dbc.entity.RolEntity;
import co.com.camilo.r2dbc.helper.ReactiveAdapterOperations;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RolRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        Integer,
        RolReactiveRepository
        > implements RolRepository {

    public RolRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Rol.class));
    }

    @Override
    public Mono<Rol> findById(int id) {
        return super.findById(id);
    }

    @Override
    public Mono<Rol> save(Rol rol) {
        return super.save(rol);
    }
}
