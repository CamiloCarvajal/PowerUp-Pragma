package co.com.camilo.usecase.solicitud;

import co.com.camilo.model.exceptions.PrestamoNotFoundException;
import co.com.camilo.model.solicitud.*;
import co.com.camilo.model.solicitud.gateways.PrestamoRepository;
import co.com.camilo.model.solicitud.gateways.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SolicitudUseCase Tests")
class SolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    private SolicitudUseCase solicitudUseCase;

    private Prestamo prestamo;
    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        solicitudUseCase = new SolicitudUseCase(solicitudRepository, prestamoRepository);

        prestamo = Prestamo.builder()
                .id(1)
                .nombre("Préstamo Personal")
                .montoMinimo(1000000L)
                .montoMaximo(50000000L)
                .tasaInteres(12.5)
                .validacionAutomatica(true)
                .build();

        solicitud = Solicitud.builder()
                .id(0)
                .monto(2000000L)
                .plazo(24)
                .email("test@example.com")
                .prestamo(Prestamo.builder().id(1).build())
                .build();
    }

    @Nested
    @DisplayName("crearSolicitud Tests")
    class CrearSolicitudTests {

        @Test
        @DisplayName("Should create solicitud successfully when prestamo exists")
        void shouldCreateSolicitudSuccessfullyWhenPrestamoExists() {
            // Given
            Solicitud expectedSolicitud = Solicitud.builder()
                    .id(1)
                    .monto(2000000L)
                    .plazo(24)
                    .email("test@example.com")
                    .estado(Estado.builder().id(1).build())
                    .prestamo(prestamo)
                    .build();

            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(expectedSolicitud));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitud);

            // Then
            StepVerifier.create(result)
                    .expectNext(expectedSolicitud)
                    .verifyComplete();

            verify(prestamoRepository, times(2)).findById(1);
            verify(solicitudRepository, times(1)).save(any(Solicitud.class));
        }

        @Test
        @DisplayName("Should throw PrestamoNotFoundException when prestamo does not exist")
        void shouldThrowPrestamoNotFoundExceptionWhenPrestamoDoesNotExist() {
            // Given
            when(prestamoRepository.findById(1)).thenReturn(Mono.empty());

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitud);

            // Then
            StepVerifier.create(result)
                    .expectError(PrestamoNotFoundException.class)
                    .verify();

            verify(prestamoRepository, times(1)).findById(1);
            verify(solicitudRepository, never()).save(any(Solicitud.class));
        }

        @Test
        @DisplayName("Should propagate repository errors")
        void shouldPropagateRepositoryErrors() {
            // Given
            RuntimeException repositoryError = new RuntimeException("Database connection error");
            when(prestamoRepository.findById(1)).thenReturn(Mono.error(repositoryError));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitud);

            // Then
            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(prestamoRepository, times(1)).findById(1);
            verify(solicitudRepository, never()).save(any(Solicitud.class));
        }

        @Test
        @DisplayName("Should handle solicitud with different prestamo ID")
        void shouldHandleSolicitudWithDifferentPrestamoId() {
            // Given
            Solicitud solicitudWithDifferentPrestamo = Solicitud.builder()
                    .monto(3000000L)
                    .plazo(36)
                    .email("different@example.com")
                    .prestamo(Prestamo.builder().id(2).build())
                    .build();

            Prestamo differentPrestamo = Prestamo.builder()
                    .id(2)
                    .nombre("Préstamo Hipotecario")
                    .montoMinimo(5000000L)
                    .montoMaximo(100000000L)
                    .tasaInteres(8.5)
                    .validacionAutomatica(false)
                    .build();

            Solicitud expectedSolicitud = Solicitud.builder()
                    .id(1)
                    .monto(3000000L)
                    .plazo(36)
                    .email("different@example.com")
                    .estado(Estado.builder().id(1).build())
                    .prestamo(differentPrestamo)
                    .build();

            when(prestamoRepository.findById(2)).thenReturn(Mono.just(differentPrestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(expectedSolicitud));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitudWithDifferentPrestamo);

            // Then
            StepVerifier.create(result)
                    .expectNext(expectedSolicitud)
                    .verifyComplete();

            verify(prestamoRepository, times(2)).findById(2);
            verify(solicitudRepository, times(1)).save(any(Solicitud.class));
        }
    }

    @Nested
    @DisplayName("validarTipoPrestamo Tests")
    class ValidarTipoPrestamoTests {

        @Test
        @DisplayName("Should validate prestamo successfully when exists")
        void shouldValidatePrestamoSuccessfullyWhenExists() {
            // Given
            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class)))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitud);

            // Then
            StepVerifier.create(result)
                    .expectNextMatches(savedSolicitud ->
                            savedSolicitud.getPrestamo().getId() == 1 &&
                                    savedSolicitud.getEstado().getId() == 1
                    )
                    .verifyComplete();

            verify(prestamoRepository, atLeastOnce()).findById(1);
            verify(solicitudRepository, atLeastOnce()).save(any(Solicitud.class));
        }

        @Test
        @DisplayName("Should throw PrestamoNotFoundException when prestamo does not exist")
        void shouldThrowPrestamoNotFoundExceptionWhenPrestamoDoesNotExist() {
            // Given
            when(prestamoRepository.findById(1)).thenReturn(Mono.empty());

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitud);

            // Then
            StepVerifier.create(result)
                    .expectError(PrestamoNotFoundException.class)
                    .verify();

            verify(prestamoRepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Should handle prestamo validation with zero ID")
        void shouldHandlePrestamoValidationWithZeroId() {
            // Given
            Solicitud solicitudWithZeroPrestamoId = Solicitud.builder()
                    .monto(1000000L)
                    .plazo(12)
                    .email("test@example.com")
                    .prestamo(Prestamo.builder().id(0).build())
                    .build();

            when(prestamoRepository.findById(0)).thenReturn(Mono.empty());

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitudWithZeroPrestamoId);

            // Then
            StepVerifier.create(result)
                    .expectError(PrestamoNotFoundException.class)
                    .verify();

            verify(prestamoRepository, times(1)).findById(0);
        }
    }

    @Nested
    @DisplayName("crearSolicitudConEstado Tests")
    class CrearSolicitudConEstadoTests {

        @Test
        @DisplayName("Should create solicitud with estado pendiente")
        void shouldCreateSolicitudWithEstadoPendiente() {
            // Given
            Solicitud expectedSolicitud = Solicitud.builder()
                    .id(1)
                    .monto(2000000L)
                    .plazo(24)
                    .email("test@example.com")
                    .estado(Estado.builder().id(1).build())
                    .prestamo(prestamo)
                    .build();

            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(expectedSolicitud));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitud);

            // Then
            StepVerifier.create(result)
                    .assertNext(savedSolicitud -> {
                        assertThat(savedSolicitud.getEstado()).isNotNull();
                        assertThat(savedSolicitud.getEstado().getId()).isEqualTo(1);
                        assertThat(savedSolicitud.getPrestamo()).isEqualTo(prestamo);
                        assertThat(savedSolicitud.getMonto()).isEqualTo(2000000L);
                        assertThat(savedSolicitud.getPlazo()).isEqualTo(24);
                        assertThat(savedSolicitud.getEmail()).isEqualTo("test@example.com");
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should preserve original solicitud data")
        void shouldPreserveOriginalSolicitudData() {
            // Given
            Solicitud originalSolicitud = Solicitud.builder()
                    .monto(5000000L)
                    .plazo(48)
                    .email("preserve@example.com")
                    .prestamo(Prestamo.builder().id(1).build())
                    .build();

            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(originalSolicitud));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(originalSolicitud);

            // Then
            StepVerifier.create(result)
                    .assertNext(savedSolicitud -> {
                        assertThat(savedSolicitud.getMonto()).isEqualTo(5000000L);
                        assertThat(savedSolicitud.getPlazo()).isEqualTo(48);
                        assertThat(savedSolicitud.getEmail()).isEqualTo("preserve@example.com");
                    })
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should complete full flow successfully")
        void shouldCompleteFullFlowSuccessfully() {
            // Given
            Solicitud inputSolicitud = Solicitud.builder()
                    .monto(3000000L)
                    .plazo(36)
                    .email("integration@example.com")
                    .prestamo(Prestamo.builder().id(1).build())
                    .build();

            Solicitud savedSolicitud = Solicitud.builder()
                    .id(1)
                    .monto(3000000L)
                    .plazo(36)
                    .email("integration@example.com")
                    .estado(Estado.builder().id(1).build())
                    .prestamo(prestamo)
                    .build();

            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(savedSolicitud));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(inputSolicitud);

            // Then
            StepVerifier.create(result)
                    .assertNext(finalSolicitud -> {
                        assertThat(finalSolicitud.getId()).isEqualTo(1);
                        assertThat(finalSolicitud.getMonto()).isEqualTo(3000000L);
                        assertThat(finalSolicitud.getPlazo()).isEqualTo(36);
                        assertThat(finalSolicitud.getEmail()).isEqualTo("integration@example.com");
                        assertThat(finalSolicitud.getEstado().getId()).isEqualTo(1);
                        assertThat(finalSolicitud.getPrestamo()).isEqualTo(prestamo);
                    })
                    .verifyComplete();

            verify(prestamoRepository, times(2)).findById(1);
            verify(solicitudRepository, times(1)).save(any(Solicitud.class));
        }

        @Test
        @DisplayName("Should handle repository save error")
        void shouldHandleRepositorySaveError() {
            // Given
            RuntimeException saveError = new RuntimeException("Save operation failed");
            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.error(saveError));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitud);

            // Then
            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(prestamoRepository, times(2)).findById(1);
            verify(solicitudRepository, times(1)).save(any(Solicitud.class));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle solicitud with null prestamo")
        void shouldHandleSolicitudWithNullPrestamo() {
            // Given
            Solicitud solicitudWithNullPrestamo = Solicitud.builder()
                    .monto(1000000L)
                    .plazo(12)
                    .email("test@example.com")
                    .prestamo(null)
                    .build();

            // When & Then
            assertThatThrownBy(() -> solicitudUseCase.crearSolicitud(solicitudWithNullPrestamo))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle solicitud with null email")
        void shouldHandleSolicitudWithNullEmail() {
            // Given
            Solicitud solicitudWithNullEmail = Solicitud.builder()
                    .monto(1000000L)
                    .plazo(12)
                    .email(null)
                    .prestamo(Prestamo.builder().id(1).build())
                    .build();

            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(solicitudWithNullEmail));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitudWithNullEmail);

            // Then
            StepVerifier.create(result)
                    .expectNextCount(1)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should handle solicitud with zero monto")
        void shouldHandleSolicitudWithZeroMonto() {
            // Given
            Solicitud solicitudWithZeroMonto = Solicitud.builder()
                    .monto(0L)
                    .plazo(12)
                    .email("test@example.com")
                    .prestamo(Prestamo.builder().id(1).build())
                    .build();

            when(prestamoRepository.findById(1)).thenReturn(Mono.just(prestamo));
            when(solicitudRepository.save(any(Solicitud.class))).thenReturn(Mono.just(solicitudWithZeroMonto));

            // When
            Mono<Solicitud> result = solicitudUseCase.crearSolicitud(solicitudWithZeroMonto);

            // Then
            StepVerifier.create(result)
                    .expectNextCount(1)
                    .verifyComplete();
        }
    }
}
