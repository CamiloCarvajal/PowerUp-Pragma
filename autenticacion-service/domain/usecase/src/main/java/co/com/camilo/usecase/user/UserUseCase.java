package co.com.camilo.usecase.user;

import reactor.core.publisher.Mono;
import co.com.camilo.model.user.User;
import co.com.camilo.model.autenticacion.gateways.PasswordEncoder;
import co.com.camilo.model.user.gateways.UserRepository;


public class UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> saveUser(User user) {
        // Encode password before saving
        User userWithEncodedPassword = user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        return userRepository.findByEmail(user.getCorreoElectronico())
                .flatMap(existingUser ->
                        Mono.<User>error(new IllegalStateException("El usuario ya existe.")))
                .switchIfEmpty(userRepository.save(userWithEncodedPassword));
    }


    public Mono<User> findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            Mono.error(new IllegalArgumentException("El email no puede ser nulo o vacío"));
        }
        
        return userRepository.findByEmail(email);
    }
}
