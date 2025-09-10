package co.com.camilo.r2dbc;

import co.com.camilo.r2dbc.entity.UserEntity;

import reactor.core.publisher.Mono;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Integer>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByCorreoElectronico(String email);

}
