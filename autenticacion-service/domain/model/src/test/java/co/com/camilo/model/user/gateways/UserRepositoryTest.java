package co.com.camilo.model.user.gateways;

import co.com.camilo.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepository Gateway Tests")
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .nombre("Juan")
                .apellido("Pérez")
                .correoElectronico("juan@email.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123 #45-67")
                .telefono("+57 300 123 4567")
                .salarioBase(150000)
                .build();
    }

    @Nested
    @DisplayName("Save Method Tests")
    class SaveMethodTests {

        @Test
        @DisplayName("Should save user successfully")
        void shouldSaveUserSuccessfully() {
            // Arrange
            when(userRepository.save(any(User.class)))
                    .thenReturn(Mono.just(testUser));

            // Act
            Mono<User> result = userRepository.save(testUser);

            // Assert
            StepVerifier.create(result)
                    .expectNext(testUser)
                    .verifyComplete();

            verify(userRepository).save(testUser);
        }

        @Test
        @DisplayName("Should handle save errors")
        void shouldHandleSaveErrors() {
            // Arrange
            RuntimeException error = new RuntimeException("Save failed");
            when(userRepository.save(any(User.class)))
                    .thenReturn(Mono.error(error));

            // Act & Assert
            StepVerifier.create(userRepository.save(testUser))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).save(testUser);
        }
    }

    @Nested
    @DisplayName("Find By Email Method Tests")
    class FindByEmailMethodTests {

        @Test
        @DisplayName("Should find user by email successfully")
        void shouldFindUserByEmailSuccessfully() {
            // Arrange
            String email = "juan@email.com";
            when(userRepository.findByEmail(email))
                    .thenReturn(Mono.just(testUser));

            // Act
            Mono<User> result = userRepository.findByEmail(email);

            // Assert
            StepVerifier.create(result)
                    .expectNext(testUser)
                    .verifyComplete();

            verify(userRepository).findByEmail(email);
        }

        @Test
        @DisplayName("Should return empty when user not found")
        void shouldReturnEmptyWhenUserNotFound() {
            // Arrange
            String email = "nonexistent@email.com";
            when(userRepository.findByEmail(email))
                    .thenReturn(Mono.empty());

            // Act
            Mono<User> result = userRepository.findByEmail(email);

            // Assert
            StepVerifier.create(result)
                    .verifyComplete();

            verify(userRepository).findByEmail(email);
        }

        @Test
        @DisplayName("Should handle find by email errors")
        void shouldHandleFindByEmailErrors() {
            // Arrange
            String email = "juan@email.com";
            RuntimeException error = new RuntimeException("Find failed");
            when(userRepository.findByEmail(email))
                    .thenReturn(Mono.error(error));

            // Act & Assert
            StepVerifier.create(userRepository.findByEmail(email))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).findByEmail(email);
        }
    }
}