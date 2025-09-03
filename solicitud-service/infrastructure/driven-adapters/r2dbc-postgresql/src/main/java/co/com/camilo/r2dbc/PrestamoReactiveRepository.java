package co.com.camilo.r2dbc;

import co.com.camilo.r2dbc.entity.PrestamoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface PrestamoReactiveRepository extends ReactiveCrudRepository<PrestamoEntity, Integer>, ReactiveQueryByExampleExecutor<PrestamoEntity> {

}
