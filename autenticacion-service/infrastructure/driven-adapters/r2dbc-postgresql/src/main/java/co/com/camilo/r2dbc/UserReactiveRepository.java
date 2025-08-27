package co.com.camilo.r2dbc;

import co.com.camilo.r2dbc.entity.UserEntity;

import reactor.core.publisher.Mono;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Integer>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByCorreoElectronico(String email);

}
