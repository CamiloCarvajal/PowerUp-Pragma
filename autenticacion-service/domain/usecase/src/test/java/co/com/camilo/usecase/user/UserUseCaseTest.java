package co.com.camilo.usecase.user;

import co.com.camilo.model.user.User;
import co.com.camilo.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserUseCase Tests")
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User validUser;
    private User existingUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .id(0)
                .nombre("Juan")
                .apellido("Pérez")
                .correoElectronico("juan@email.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123 #45-67")
                .telefono("+57 300 123 4567")
                .salarioBase(150000)
                .build();

        existingUser = User.builder()
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
    @DisplayName("Save User Tests")
    class SaveUserTests {

        @Test
        @DisplayName("Should save user successfully when user does not exist")
        void shouldSaveUserSuccessfullyWhenUserDoesNotExist() {
            // Arrange
            when(userRepository.findByEmail(validUser.getCorreoElectronico()))
                    .thenReturn(Mono.empty());
            when(userRepository.save(validUser))
                    .thenReturn(Mono.just(existingUser));

            // Act
            Mono<User> result = userUseCase.saveUser(validUser);

            // Assert
            StepVerifier.create(result)
                    .expectNext(existingUser)
                    .verifyComplete();

            verify(userRepository).findByEmail(validUser.getCorreoElectronico());
            verify(userRepository).save(validUser);
        }

        @Test
        @DisplayName("Should throw exception when user already exists")
        void shouldThrowExceptionWhenUserAlreadyExists() {
            // Arrange
            when(userRepository.findByEmail(validUser.getCorreoElectronico()))
                    .thenReturn(Mono.just(existingUser));

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(validUser))
                    .expectError(IllegalStateException.class)
                    .verify();

            verify(userRepository).findByEmail(validUser.getCorreoElectronico());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user is null")
        void shouldThrowExceptionWhenUserIsNull() {
            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(null))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            // Arrange
            User userWithoutEmail = validUser.toBuilder()
                    .correoElectronico(null)
                    .build();

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(userWithoutEmail))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when email is empty")
        void shouldThrowExceptionWhenEmailIsEmpty() {
            // Arrange
            User userWithEmptyEmail = validUser.toBuilder()
                    .correoElectronico("")
                    .build();

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(userWithEmptyEmail))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when email is blank")
        void shouldThrowExceptionWhenEmailIsBlank() {
            // Arrange
            User userWithBlankEmail = validUser.toBuilder()
                    .correoElectronico("   ")
                    .build();

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(userWithBlankEmail))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when salary is negative")
        void shouldThrowExceptionWhenSalaryIsNegative() {
            // Arrange
            User userWithNegativeSalary = validUser.toBuilder()
                    .salarioBase(-1000)
                    .build();

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(userWithNegativeSalary))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            // Arrange
            User userWithoutName = validUser.toBuilder()
                    .nombre(null)
                    .build();

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(userWithoutName))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when name is empty")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // Arrange
            User userWithEmptyName = validUser.toBuilder()
                    .nombre("")
                    .build();

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(userWithEmptyName))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when salary is below minimum")
        void shouldThrowExceptionWhenSalaryIsBelowMinimum() {
            // Arrange
            User userWithLowSalary = validUser.toBuilder()
                    .salarioBase(50000)
                    .build();

            when(userRepository.findByEmail(userWithLowSalary.getCorreoElectronico()))
                    .thenReturn(Mono.empty());

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(userWithLowSalary))
                    .expectError(IllegalStateException.class)
                    .verify();

            verify(userRepository).findByEmail(userWithLowSalary.getCorreoElectronico());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Find User By Email Tests")
    class FindUserByEmailTests {

        @Test
        @DisplayName("Should find user by email successfully")
        void shouldFindUserByEmailSuccessfully() {
            // Arrange
            String email = "juan@email.com";
            when(userRepository.findByEmail(email))
                    .thenReturn(Mono.just(existingUser));

            // Act
            Mono<User> result = userUseCase.findUserByEmail(email);

            // Assert
            StepVerifier.create(result)
                    .expectNext(existingUser)
                    .verifyComplete();

            verify(userRepository).findByEmail(email);
        }

        @Test
        @DisplayName("Should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            // Act & Assert
            StepVerifier.create(userUseCase.findUserByEmail(null))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
        }

        @Test
        @DisplayName("Should throw exception when email is empty")
        void shouldThrowExceptionWhenEmailIsEmpty() {
            // Act & Assert
            StepVerifier.create(userUseCase.findUserByEmail(""))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
        }

        @Test
        @DisplayName("Should throw exception when email is blank")
        void shouldThrowExceptionWhenEmailIsBlank() {
            // Act & Assert
            StepVerifier.create(userUseCase.findUserByEmail("   "))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(userRepository, never()).findByEmail(any());
        }
    }

    @Nested
    @DisplayName("Repository Error Handling Tests")
    class RepositoryErrorHandlingTests {

        @Test
        @DisplayName("Should propagate repository errors during save")
        void shouldPropagateRepositoryErrorsDuringSave() {
            // Arrange
            RuntimeException repositoryError = new RuntimeException("Database connection failed");
            when(userRepository.findByEmail(validUser.getCorreoElectronico()))
                    .thenReturn(Mono.empty());
            when(userRepository.save(validUser))
                    .thenReturn(Mono.error(repositoryError));

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(validUser))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).findByEmail(validUser.getCorreoElectronico());
            verify(userRepository).save(validUser);
        }

        @Test
        @DisplayName("Should propagate repository errors during findByEmail")
        void shouldPropagateRepositoryErrorsDuringFindByEmail() {
            // Arrange
            RuntimeException repositoryError = new RuntimeException("Database connection failed");
            when(userRepository.findByEmail(validUser.getCorreoElectronico()))
                    .thenReturn(Mono.error(repositoryError));

            // Act & Assert
            StepVerifier.create(userUseCase.saveUser(validUser))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).findByEmail(validUser.getCorreoElectronico());
            verify(userRepository, never()).save(any());
        }
    }
}