package co.com.camilo.r2dbc;

import co.com.camilo.model.solicitud.Prestamo;
import co.com.camilo.model.solicitud.gateways.PrestamoRepository;
import co.com.camilo.r2dbc.entity.PrestamoEntity;
import co.com.camilo.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PrestamoRepositoryAdapter extends ReactiveAdapterOperations<
        Prestamo,
        PrestamoEntity,
        Integer,
        PrestamoReactiveRepository
        > implements PrestamoRepository {
    public PrestamoRepositoryAdapter(PrestamoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Prestamo.class));
    }

    @Override
    public Mono<Prestamo> findById(int id) {
        return super.findById(id);
    }
}
