package co.com.camilo.model.user.gateways;

import co.com.camilo.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    
    Mono<User> save(User user);

}
