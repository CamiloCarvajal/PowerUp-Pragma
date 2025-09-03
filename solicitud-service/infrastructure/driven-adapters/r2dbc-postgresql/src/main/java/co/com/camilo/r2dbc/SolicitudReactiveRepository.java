package co.com.camilo.r2dbc;

import co.com.camilo.r2dbc.entity.SolicitudEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Integer>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

}
