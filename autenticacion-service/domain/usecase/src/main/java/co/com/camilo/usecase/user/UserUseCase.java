package co.com.camilo.usecase.user;

import co.com.camilo.model.user.User;
import co.com.camilo.model.user.gateways.UserRepository;

import reactor.core.publisher.Mono;


//@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> saveUser(User user) {

        return userRepository.findByEmail(user.getCorreoElectronico())
                .flatMap(existingUser ->
                        Mono.<User>error(new IllegalStateException("El usuario ya existe.")))
                .switchIfEmpty(userRepository.save(user));
    }


    public Mono<User> findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        
        return userRepository.findByEmail(email);
    }
}
