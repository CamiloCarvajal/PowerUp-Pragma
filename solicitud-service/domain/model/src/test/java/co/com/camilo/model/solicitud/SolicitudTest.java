package co.com.camilo.model.solicitud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Solicitud Domain Entity Tests")
class SolicitudTest {

    private Prestamo prestamo;
    private Estado estado;

    @BeforeEach
    void setUp() {
        prestamo = Prestamo.builder()
                .id(1)
                .nombre("Préstamo Personal")
                .montoMinimo(1000000L)
                .montoMaximo(50000000L)
                .tasaInteres(12.5)
                .validacionAutomatica(true)
                .build();

        estado = Estado.builder()
                .id(1)
                .nombre("Pendiente")
                .descripcion("Solicitud pendiente de revisión")
                .build();
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create Solicitud with all fields using builder")
        void shouldCreateSolicitudWithAllFieldsUsingBuilder() {
            // Given
            int id = 1;
            Long monto = 2000000L;
            int plazo = 24;
            String email = "test@example.com";

            // When
            Solicitud solicitud = Solicitud.builder()
                    .id(id)
                    .monto(monto)
                    .plazo(plazo)
                    .email(email)
                    .estado(estado)
                    .prestamo(prestamo)
                    .build();

            // Then
            assertThat(solicitud).isNotNull();
            assertThat(solicitud.getId()).isEqualTo(id);
            assertThat(solicitud.getMonto()).isEqualTo(monto);
            assertThat(solicitud.getPlazo()).isEqualTo(plazo);
            assertThat(solicitud.getEmail()).isEqualTo(email);
            assertThat(solicitud.getEstado()).isEqualTo(estado);
            assertThat(solicitud.getPrestamo()).isEqualTo(prestamo);
        }

        @Test
        @DisplayName("Should create Solicitud with minimal fields using builder")
        void shouldCreateSolicitudWithMinimalFieldsUsingBuilder() {
            // When
            Solicitud solicitud = Solicitud.builder()
                    .monto(1000000L)
                    .plazo(12)
                    .email("minimal@example.com")
                    .build();

            // Then
            assertThat(solicitud).isNotNull();
            assertThat(solicitud.getId()).isEqualTo(0);
            assertThat(solicitud.getMonto()).isEqualTo(1000000L);
            assertThat(solicitud.getPlazo()).isEqualTo(12);
            assertThat(solicitud.getEmail()).isEqualTo("minimal@example.com");
            assertThat(solicitud.getEstado()).isNull();
            assertThat(solicitud.getPrestamo()).isNull();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create Solicitud with no-args constructor")
        void shouldCreateSolicitudWithNoArgsConstructor() {
            // When
            Solicitud solicitud = new Solicitud();

            // Then
            assertThat(solicitud).isNotNull();
            assertThat(solicitud.getId()).isEqualTo(0);
            assertThat(solicitud.getMonto()).isNull();
            assertThat(solicitud.getPlazo()).isEqualTo(0);
            assertThat(solicitud.getEmail()).isNull();
            assertThat(solicitud.getEstado()).isNull();
            assertThat(solicitud.getPrestamo()).isNull();
        }

        @Test
        @DisplayName("Should create Solicitud with all-args constructor")
        void shouldCreateSolicitudWithAllArgsConstructor() {
            // Given
            int id = 1;
            Long monto = 2000000L;
            int plazo = 24;
            String email = "test@example.com";

            // When
            Solicitud solicitud = new Solicitud(id, monto, plazo, email, estado, prestamo);

            // Then
            assertThat(solicitud).isNotNull();
            assertThat(solicitud.getId()).isEqualTo(id);
            assertThat(solicitud.getMonto()).isEqualTo(monto);
            assertThat(solicitud.getPlazo()).isEqualTo(plazo);
            assertThat(solicitud.getEmail()).isEqualTo(email);
            assertThat(solicitud.getEstado()).isEqualTo(estado);
            assertThat(solicitud.getPrestamo()).isEqualTo(prestamo);
        }
    }

    @Nested
    @DisplayName("Setters and Getters Tests")
    class SettersAndGettersTests {

        @Test
        @DisplayName("Should set and get all fields correctly")
        void shouldSetAndGetAllFieldsCorrectly() {
            // Given
            Solicitud solicitud = new Solicitud();
            int id = 1;
            Long monto = 2000000L;
            int plazo = 24;
            String email = "test@example.com";

            // When
            solicitud.setId(id);
            solicitud.setMonto(monto);
            solicitud.setPlazo(plazo);
            solicitud.setEmail(email);
            solicitud.setEstado(estado);
            solicitud.setPrestamo(prestamo);

            // Then
            assertThat(solicitud.getId()).isEqualTo(id);
            assertThat(solicitud.getMonto()).isEqualTo(monto);
            assertThat(solicitud.getPlazo()).isEqualTo(plazo);
            assertThat(solicitud.getEmail()).isEqualTo(email);
            assertThat(solicitud.getEstado()).isEqualTo(estado);
            assertThat(solicitud.getPrestamo()).isEqualTo(prestamo);
        }
    }

    @Nested
    @DisplayName("ToBuilder Tests")
    class ToBuilderTests {

        @Test
        @DisplayName("Should create new instance using toBuilder")
        void shouldCreateNewInstanceUsingToBuilder() {
            // Given
            Solicitud originalSolicitud = Solicitud.builder()
                    .id(1)
                    .monto(2000000L)
                    .plazo(24)
                    .email("original@example.com")
                    .estado(estado)
                    .prestamo(prestamo)
                    .build();

            // When
            Solicitud modifiedSolicitud = originalSolicitud.toBuilder()
                    .monto(3000000L)
                    .plazo(36)
                    .email("modified@example.com")
                    .build();

            // Then
            assertThat(modifiedSolicitud).isNotNull();
            assertThat(modifiedSolicitud.getId()).isEqualTo(1);
            assertThat(modifiedSolicitud.getMonto()).isEqualTo(3000000L);
            assertThat(modifiedSolicitud.getPlazo()).isEqualTo(36);
            assertThat(modifiedSolicitud.getEmail()).isEqualTo("modified@example.com");
            assertThat(modifiedSolicitud.getEstado()).isEqualTo(estado);
            assertThat(modifiedSolicitud.getPrestamo()).isEqualTo(prestamo);

            // Original should remain unchanged
            assertThat(originalSolicitud.getMonto()).isEqualTo(2000000L);
            assertThat(originalSolicitud.getPlazo()).isEqualTo(24);
            assertThat(originalSolicitud.getEmail()).isEqualTo("original@example.com");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null values correctly")
        void shouldHandleNullValuesCorrectly() {
            // When
            Solicitud solicitud = Solicitud.builder()
                    .id(1)
                    .monto(null)
                    .plazo(0)
                    .email(null)
                    .estado(null)
                    .prestamo(null)
                    .build();

            // Then
            assertThat(solicitud.getId()).isEqualTo(1);
            assertThat(solicitud.getMonto()).isNull();
            assertThat(solicitud.getPlazo()).isEqualTo(0);
            assertThat(solicitud.getEmail()).isNull();
            assertThat(solicitud.getEstado()).isNull();
            assertThat(solicitud.getPrestamo()).isNull();
        }

        @Test
        @DisplayName("Should handle zero and negative values")
        void shouldHandleZeroAndNegativeValues() {
            // When
            Solicitud solicitud = Solicitud.builder()
                    .id(-1)
                    .monto(0L)
                    .plazo(-5)
                    .email("test@example.com")
                    .build();

            // Then
            assertThat(solicitud.getId()).isEqualTo(-1);
            assertThat(solicitud.getMonto()).isEqualTo(0L);
            assertThat(solicitud.getPlazo()).isEqualTo(-5);
            assertThat(solicitud.getEmail()).isEqualTo("test@example.com");
        }
    }
}
