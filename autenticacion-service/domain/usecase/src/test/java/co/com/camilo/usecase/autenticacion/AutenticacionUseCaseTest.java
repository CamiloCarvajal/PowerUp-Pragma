package co.com.camilo.usecase.autenticacion;

import co.com.camilo.model.autenticacion.LoginRequest;
import co.com.camilo.model.autenticacion.TokenResponse;
import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import co.com.camilo.model.autenticacion.gateways.AutenticacionRepository;
import co.com.camilo.model.autenticacion.gateways.JwtService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutenticacionUseCase Tests")
class AutenticacionUseCaseTest {

    @Mock
    private AutenticacionRepository autenticacionRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AutenticacionUseCase autenticacionUseCase;

    private LoginRequest loginRequest;
    private UsuarioAutenticado usuarioAutenticado;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .correoElectronico("usuario@email.com")
                .password("password123")
                .build();

        usuarioAutenticado = UsuarioAutenticado.builder()
                .id(1)
                .nombre("Juan")
                .apellido("Pérez")
                .correoElectronico("usuario@email.com")
                .idRol(1)
                .nombreRol("Administrador")
                .build();

    }

    @Nested
    @DisplayName("Autenticar Usuario Tests")
    class AutenticarUsuarioTests {

        @Test
        @DisplayName("Should authenticate user successfully")
        void shouldAuthenticateUserSuccessfully() {
            // Arrange
            when(autenticacionRepository.autenticarUsuario(loginRequest.getCorreoElectronico(), loginRequest.getPassword()))
                    .thenReturn(Mono.just(usuarioAutenticado));
            when(jwtService.generarToken(usuarioAutenticado))
                    .thenReturn(Mono.just("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."));

            // Act
            Mono<TokenResponse> result = autenticacionUseCase.autenticarUsuario(loginRequest);

            // Assert
            StepVerifier.create(result)
                    .expectNextMatches(response -> 
                        response.getToken().equals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") &&
                        response.getTipoToken().equals("Bearer") &&
                        response.getTiempoExpiracion() == 86400000L &&
                        response.getUsuario().getId() == 1 &&
                        response.getUsuario().getNombre().equals("Juan")
                    )
                    .verifyComplete();

            verify(autenticacionRepository).autenticarUsuario(loginRequest.getCorreoElectronico(), loginRequest.getPassword());
            verify(jwtService).generarToken(usuarioAutenticado);
        }

        @Test
        @DisplayName("Should handle authentication repository error")
        void shouldHandleAuthenticationRepositoryError() {
            // Arrange
            RuntimeException repositoryError = new RuntimeException("Database connection failed");
            when(autenticacionRepository.autenticarUsuario(anyString(), anyString()))
                    .thenReturn(Mono.error(repositoryError));

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.autenticarUsuario(loginRequest))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(autenticacionRepository).autenticarUsuario(loginRequest.getCorreoElectronico(), loginRequest.getPassword());
            verify(jwtService, never()).generarToken(any());
        }

        @Test
        @DisplayName("Should handle JWT service error")
        void shouldHandleJwtServiceError() {
            // Arrange
            RuntimeException jwtError = new RuntimeException("JWT generation failed");
            when(autenticacionRepository.autenticarUsuario(anyString(), anyString()))
                    .thenReturn(Mono.just(usuarioAutenticado));
            when(jwtService.generarToken(usuarioAutenticado))
                    .thenReturn(Mono.error(jwtError));

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.autenticarUsuario(loginRequest))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(autenticacionRepository).autenticarUsuario(loginRequest.getCorreoElectronico(), loginRequest.getPassword());
            verify(jwtService).generarToken(usuarioAutenticado);
        }

        @Test
        @DisplayName("Should handle null login request")
        void shouldHandleNullLoginRequest() {
            // Act & Assert
            StepVerifier.create(autenticacionUseCase.autenticarUsuario(null))
                    .expectError(NullPointerException.class)
                    .verify();

            verify(autenticacionRepository, never()).autenticarUsuario(anyString(), anyString());
            verify(jwtService, never()).generarToken(any());
        }

        @Test
        @DisplayName("Should handle null email in login request")
        void shouldHandleNullEmailInLoginRequest() {
            // Arrange
            LoginRequest requestWithNullEmail = LoginRequest.builder()
                    .correoElectronico(null)
                    .password("password123")
                    .build();

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.autenticarUsuario(requestWithNullEmail))
                    .expectError(NullPointerException.class)
                    .verify();

            verify(autenticacionRepository, never()).autenticarUsuario(anyString(), anyString());
            verify(jwtService, never()).generarToken(any());
        }

        @Test
        @DisplayName("Should handle null password in login request")
        void shouldHandleNullPasswordInLoginRequest() {
            // Arrange
            LoginRequest requestWithNullPassword = LoginRequest.builder()
                    .correoElectronico("usuario@email.com")
                    .password(null)
                    .build();

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.autenticarUsuario(requestWithNullPassword))
                    .expectError(NullPointerException.class)
                    .verify();

            verify(autenticacionRepository, never()).autenticarUsuario(anyString(), anyString());
            verify(jwtService, never()).generarToken(any());
        }
    }

    @Nested
    @DisplayName("Validar Token Tests")
    class ValidarTokenTests {

        @Test
        @DisplayName("Should validate token successfully")
        void shouldValidateTokenSuccessfully() {
            // Arrange
            String token = "valid-token";
            when(jwtService.validarToken(token))
                    .thenReturn(Mono.just(usuarioAutenticado));

            // Act
            Mono<UsuarioAutenticado> result = autenticacionUseCase.validarToken(token);

            // Assert
            StepVerifier.create(result)
                    .expectNext(usuarioAutenticado)
                    .verifyComplete();

            verify(jwtService).validarToken(token);
        }

        @Test
        @DisplayName("Should handle invalid token")
        void shouldHandleInvalidToken() {
            // Arrange
            String invalidToken = "invalid-token";
            RuntimeException tokenError = new RuntimeException("Invalid token");
            when(jwtService.validarToken(invalidToken))
                    .thenReturn(Mono.error(tokenError));

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.validarToken(invalidToken))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(jwtService).validarToken(invalidToken);
        }

        @Test
        @DisplayName("Should handle null token")
        void shouldHandleNullToken() {
            // Act & Assert
            StepVerifier.create(autenticacionUseCase.validarToken(null))
                    .expectError(NullPointerException.class)
                    .verify();

            verify(jwtService, never()).validarToken(anyString());
        }

        @Test
        @DisplayName("Should handle empty token")
        void shouldHandleEmptyToken() {
            // Arrange
            String emptyToken = "";
            RuntimeException tokenError = new RuntimeException("Empty token");
            when(jwtService.validarToken(emptyToken))
                    .thenReturn(Mono.error(tokenError));

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.validarToken(emptyToken))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(jwtService).validarToken(emptyToken);
        }
    }

    @Nested
    @DisplayName("Es Token Valido Tests")
    class EsTokenValidoTests {

        @Test
        @DisplayName("Should return true for valid token")
        void shouldReturnTrueForValidToken() {
            // Arrange
            String token = "valid-token";
            when(jwtService.esTokenValido(token))
                    .thenReturn(Mono.just(true));

            // Act
            Mono<Boolean> result = autenticacionUseCase.esTokenValido(token);

            // Assert
            StepVerifier.create(result)
                    .expectNext(true)
                    .verifyComplete();

            verify(jwtService).esTokenValido(token);
        }

        @Test
        @DisplayName("Should return false for invalid token")
        void shouldReturnFalseForInvalidToken() {
            // Arrange
            String token = "invalid-token";
            when(jwtService.esTokenValido(token))
                    .thenReturn(Mono.just(false));

            // Act
            Mono<Boolean> result = autenticacionUseCase.esTokenValido(token);

            // Assert
            StepVerifier.create(result)
                    .expectNext(false)
                    .verifyComplete();

            verify(jwtService).esTokenValido(token);
        }

        @Test
        @DisplayName("Should handle JWT service error in token validation")
        void shouldHandleJwtServiceErrorInTokenValidation() {
            // Arrange
            String token = "error-token";
            RuntimeException tokenError = new RuntimeException("Token validation error");
            when(jwtService.esTokenValido(token))
                    .thenReturn(Mono.error(tokenError));

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.esTokenValido(token))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(jwtService).esTokenValido(token);
        }

        @Test
        @DisplayName("Should handle null token in token validation")
        void shouldHandleNullTokenInTokenValidation() {
            // Act & Assert
            StepVerifier.create(autenticacionUseCase.esTokenValido(null))
                    .expectError(NullPointerException.class)
                    .verify();

            verify(jwtService, never()).esTokenValido(anyString());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty email in login request")
        void shouldHandleEmptyEmailInLoginRequest() {
            // Arrange
            LoginRequest requestWithEmptyEmail = LoginRequest.builder()
                    .correoElectronico("")
                    .password("password123")
                    .build();

            RuntimeException repositoryError = new RuntimeException("Empty email");
            when(autenticacionRepository.autenticarUsuario("", "password123"))
                    .thenReturn(Mono.error(repositoryError));

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.autenticarUsuario(requestWithEmptyEmail))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(autenticacionRepository).autenticarUsuario("", "password123");
        }

        @Test
        @DisplayName("Should handle empty password in login request")
        void shouldHandleEmptyPasswordInLoginRequest() {
            // Arrange
            LoginRequest requestWithEmptyPassword = LoginRequest.builder()
                    .correoElectronico("usuario@email.com")
                    .password("")
                    .build();

            RuntimeException repositoryError = new RuntimeException("Empty password");
            when(autenticacionRepository.autenticarUsuario("usuario@email.com", ""))
                    .thenReturn(Mono.error(repositoryError));

            // Act & Assert
            StepVerifier.create(autenticacionUseCase.autenticarUsuario(requestWithEmptyPassword))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(autenticacionRepository).autenticarUsuario("usuario@email.com", "");
        }

        @Test
        @DisplayName("Should handle very long token")
        void shouldHandleVeryLongToken() {
            // Arrange
            String longToken = "a".repeat(10000);
            when(jwtService.esTokenValido(longToken))
                    .thenReturn(Mono.just(false));

            // Act
            Mono<Boolean> result = autenticacionUseCase.esTokenValido(longToken);

            // Assert
            StepVerifier.create(result)
                    .expectNext(false)
                    .verifyComplete();

            verify(jwtService).esTokenValido(longToken);
        }
    }
}
