package co.com.camilo.r2dbc.repository;

import co.com.camilo.r2dbc.entity.RolEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RolReactiveRepository extends ReactiveCrudRepository<RolEntity, Integer>, ReactiveQueryByExampleExecutor<RolEntity> {
}

