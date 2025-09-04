package co.com.camilo.r2dbc;

import co.com.camilo.model.user.User;
import co.com.camilo.model.user.gateways.UserRepository;
import co.com.camilo.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepositoryAdapter Tests")
class UserRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter repositoryAdapter;

    private User testUser;
    private UserEntity testUserEntity;

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

        testUserEntity = new UserEntity();
        testUserEntity.setId(1);
        testUserEntity.setNombre("Juan");
        testUserEntity.setApellido("Pérez");
        testUserEntity.setCorreoElectronico("juan@email.com");
        testUserEntity.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        testUserEntity.setDireccion("Calle 123 #45-67");
        testUserEntity.setTelefono("+57 300 123 4567");
        testUserEntity.setSalarioBase(150000);
    }

    @Nested
    @DisplayName("Save Method Tests")
    class SaveMethodTests {

        @Test
        @DisplayName("Should save user successfully")
        void shouldSaveUserSuccessfully() {
            // Arrange
            when(repository.save(any(UserEntity.class)))
                    .thenReturn(Mono.just(testUserEntity));
            when(mapper.map(testUserEntity, User.class))
                    .thenReturn(testUser);

            // Act
            Mono<User> result = repositoryAdapter.save(testUser);

            // Assert
            StepVerifier.create(result)
                    .expectNext(testUser)
                    .verifyComplete();

            verify(repository).save(any(UserEntity.class));
            verify(mapper).map(testUserEntity, User.class);
        }

        @Test
        @DisplayName("Should throw exception when user is null")
        void shouldThrowExceptionWhenUserIsNull() {
            // Act & Assert
            StepVerifier.create(repositoryAdapter.save(null))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(repository, never()).save(any());
            verify(mapper, never()).map(any(), any());
        }

        @Test
        @DisplayName("Should handle repository save errors")
        void shouldHandleRepositorySaveErrors() {
            // Arrange
            RuntimeException error = new RuntimeException("Save failed");
            when(repository.save(any(UserEntity.class)))
                    .thenReturn(Mono.error(error));

            // Act & Assert
            StepVerifier.create(repositoryAdapter.save(testUser))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(repository).save(any(UserEntity.class));
            verify(mapper, never()).map(any(), any());
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
            when(repository.findByCorreoElectronico(email))
                    .thenReturn(Mono.just(testUserEntity));
            when(mapper.map(testUserEntity, User.class))
                    .thenReturn(testUser);

            // Act
            Mono<User> result = repositoryAdapter.findByEmail(email);

            // Assert
            StepVerifier.create(result)
                    .expectNext(testUser)
                    .verifyComplete();

            verify(repository).findByCorreoElectronico(email);
            verify(mapper).map(testUserEntity, User.class);
        }

        @Test
        @DisplayName("Should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            // Act & Assert
            StepVerifier.create(repositoryAdapter.findByEmail(null))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(repository, never()).findByCorreoElectronico(any());
            verify(mapper, never()).map(any(), any());
        }

        @Test
        @DisplayName("Should throw exception when email is empty")
        void shouldThrowExceptionWhenEmailIsEmpty() {
            // Act & Assert
            StepVerifier.create(repositoryAdapter.findByEmail(""))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(repository, never()).findByCorreoElectronico(any());
            verify(mapper, never()).map(any(), any());
        }

        @Test
        @DisplayName("Should throw exception when email is blank")
        void shouldThrowExceptionWhenEmailIsBlank() {
            // Act & Assert
            StepVerifier.create(repositoryAdapter.findByEmail("   "))
                    .expectError(IllegalArgumentException.class)
                    .verify();

            verify(repository, never()).findByCorreoElectronico(any());
            verify(mapper, never()).map(any(), any());
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            String email = "nonexistent@email.com";
            when(repository.findByCorreoElectronico(email))
                    .thenReturn(Mono.empty());

            // Act & Assert
            StepVerifier.create(repositoryAdapter.findByEmail(email))
                    .expectError(IllegalStateException.class)
                    .verify();

            verify(repository).findByCorreoElectronico(email);
            verify(mapper, never()).map(any(), any());
        }

        @Test
        @DisplayName("Should handle repository find errors")
        void shouldHandleRepositoryFindErrors() {
            // Arrange
            String email = "juan@email.com";
            RuntimeException error = new RuntimeException("Find failed");
            when(repository.findByCorreoElectronico(email))
                    .thenReturn(Mono.error(error));

            // Act & Assert
            StepVerifier.create(repositoryAdapter.findByEmail(email))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(repository).findByCorreoElectronico(email);
            verify(mapper, never()).map(any(), any());
        }
    }

    @Nested
    @DisplayName("Mapping Tests")
    class MappingTests {

        @Test
        @DisplayName("Should map UserEntity to User correctly")
        void shouldMapUserEntityToUserCorrectly() {
            // Arrange
            when(repository.findById(1))
                    .thenReturn(Mono.just(testUserEntity));
            when(mapper.map(testUserEntity, User.class))
                    .thenReturn(testUser);

            // Act
            Mono<User> result = repositoryAdapter.findById(1);

            // Assert
            StepVerifier.create(result)
                    .expectNext(testUser)
                    .verifyComplete();

            verify(mapper).map(testUserEntity, User.class);
        }

        @Test
        @DisplayName("Should handle mapping errors")
        void shouldHandleMappingErrors() {
            // Arrange
            RuntimeException mappingError = new RuntimeException("Mapping failed");
            when(repository.findById(1))
                    .thenReturn(Mono.just(testUserEntity));
            when(mapper.map(testUserEntity, User.class))
                    .thenThrow(mappingError);

            // Act & Assert
            StepVerifier.create(repositoryAdapter.findById(1))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(mapper).map(testUserEntity, User.class);
        }
    }
}