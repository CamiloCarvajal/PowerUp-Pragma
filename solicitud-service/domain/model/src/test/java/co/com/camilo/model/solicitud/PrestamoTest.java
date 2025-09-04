package co.com.camilo.model.solicitud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Prestamo Domain Entity Tests")
class PrestamoTest {

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create Prestamo with all fields using builder")
        void shouldCreatePrestamoWithAllFieldsUsingBuilder() {
            // Given
            int id = 1;
            String nombre = "Préstamo Personal";
            long montoMinimo = 1000000L;
            long montoMaximo = 50000000L;
            double tasaInteres = 12.5;
            Boolean validacionAutomatica = true;

            // When
            Prestamo prestamo = Prestamo.builder()
                    .id(id)
                    .nombre(nombre)
                    .montoMinimo(montoMinimo)
                    .montoMaximo(montoMaximo)
                    .tasaInteres(tasaInteres)
                    .validacionAutomatica(validacionAutomatica)
                    .build();

            // Then
            assertThat(prestamo).isNotNull();
            assertThat(prestamo.getId()).isEqualTo(id);
            assertThat(prestamo.getNombre()).isEqualTo(nombre);
            assertThat(prestamo.getMontoMinimo()).isEqualTo(montoMinimo);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(montoMaximo);
            assertThat(prestamo.getTasaInteres()).isEqualTo(tasaInteres);
            assertThat(prestamo.getValidacionAutomatica()).isEqualTo(validacionAutomatica);
        }

        @Test
        @DisplayName("Should create Prestamo with minimal fields using builder")
        void shouldCreatePrestamoWithMinimalFieldsUsingBuilder() {
            // When
            Prestamo prestamo = Prestamo.builder()
                    .nombre("Préstamo Básico")
                    .montoMinimo(500000L)
                    .build();

            // Then
            assertThat(prestamo).isNotNull();
            assertThat(prestamo.getId()).isEqualTo(0);
            assertThat(prestamo.getNombre()).isEqualTo("Préstamo Básico");
            assertThat(prestamo.getMontoMinimo()).isEqualTo(500000L);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(0L);
            assertThat(prestamo.getTasaInteres()).isEqualTo(0.0);
            assertThat(prestamo.getValidacionAutomatica()).isNull();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create Prestamo with no-args constructor")
        void shouldCreatePrestamoWithNoArgsConstructor() {
            // When
            Prestamo prestamo = new Prestamo();

            // Then
            assertThat(prestamo).isNotNull();
            assertThat(prestamo.getId()).isEqualTo(0);
            assertThat(prestamo.getNombre()).isNull();
            assertThat(prestamo.getMontoMinimo()).isEqualTo(0L);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(0L);
            assertThat(prestamo.getTasaInteres()).isEqualTo(0.0);
            assertThat(prestamo.getValidacionAutomatica()).isNull();
        }

        @Test
        @DisplayName("Should create Prestamo with all-args constructor")
        void shouldCreatePrestamoWithAllArgsConstructor() {
            // Given
            int id = 1;
            String nombre = "Préstamo Personal";
            long montoMinimo = 1000000L;
            long montoMaximo = 50000000L;
            double tasaInteres = 12.5;
            Boolean validacionAutomatica = true;

            // When
            Prestamo prestamo = new Prestamo(id, nombre, montoMinimo, montoMaximo, tasaInteres, validacionAutomatica);

            // Then
            assertThat(prestamo).isNotNull();
            assertThat(prestamo.getId()).isEqualTo(id);
            assertThat(prestamo.getNombre()).isEqualTo(nombre);
            assertThat(prestamo.getMontoMinimo()).isEqualTo(montoMinimo);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(montoMaximo);
            assertThat(prestamo.getTasaInteres()).isEqualTo(tasaInteres);
            assertThat(prestamo.getValidacionAutomatica()).isEqualTo(validacionAutomatica);
        }
    }

    @Nested
    @DisplayName("Setters and Getters Tests")
    class SettersAndGettersTests {

        @Test
        @DisplayName("Should set and get all fields correctly")
        void shouldSetAndGetAllFieldsCorrectly() {
            // Given
            Prestamo prestamo = new Prestamo();
            int id = 1;
            String nombre = "Préstamo Personal";
            long montoMinimo = 1000000L;
            long montoMaximo = 50000000L;
            double tasaInteres = 12.5;
            Boolean validacionAutomatica = true;

            // When
            prestamo.setId(id);
            prestamo.setNombre(nombre);
            prestamo.setMontoMinimo(montoMinimo);
            prestamo.setMontoMaximo(montoMaximo);
            prestamo.setTasaInteres(tasaInteres);
            prestamo.setValidacionAutomatica(validacionAutomatica);

            // Then
            assertThat(prestamo.getId()).isEqualTo(id);
            assertThat(prestamo.getNombre()).isEqualTo(nombre);
            assertThat(prestamo.getMontoMinimo()).isEqualTo(montoMinimo);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(montoMaximo);
            assertThat(prestamo.getTasaInteres()).isEqualTo(tasaInteres);
            assertThat(prestamo.getValidacionAutomatica()).isEqualTo(validacionAutomatica);
        }
    }

    @Nested
    @DisplayName("ToBuilder Tests")
    class ToBuilderTests {

        @Test
        @DisplayName("Should create new instance using toBuilder")
        void shouldCreateNewInstanceUsingToBuilder() {
            // Given
            Prestamo originalPrestamo = Prestamo.builder()
                    .id(1)
                    .nombre("Préstamo Original")
                    .montoMinimo(1000000L)
                    .montoMaximo(50000000L)
                    .tasaInteres(12.5)
                    .validacionAutomatica(true)
                    .build();

            // When
            Prestamo modifiedPrestamo = originalPrestamo.toBuilder()
                    .nombre("Préstamo Modificado")
                    .montoMinimo(2000000L)
                    .tasaInteres(15.0)
                    .build();

            // Then
            assertThat(modifiedPrestamo).isNotNull();
            assertThat(modifiedPrestamo.getId()).isEqualTo(1);
            assertThat(modifiedPrestamo.getNombre()).isEqualTo("Préstamo Modificado");
            assertThat(modifiedPrestamo.getMontoMinimo()).isEqualTo(2000000L);
            assertThat(modifiedPrestamo.getMontoMaximo()).isEqualTo(50000000L);
            assertThat(modifiedPrestamo.getTasaInteres()).isEqualTo(15.0);
            assertThat(modifiedPrestamo.getValidacionAutomatica()).isEqualTo(true);

            // Original should remain unchanged
            assertThat(originalPrestamo.getNombre()).isEqualTo("Préstamo Original");
            assertThat(originalPrestamo.getMontoMinimo()).isEqualTo(1000000L);
            assertThat(originalPrestamo.getTasaInteres()).isEqualTo(12.5);
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should validate monto range correctly")
        void shouldValidateMontoRangeCorrectly() {
            // Given
            Prestamo prestamo = Prestamo.builder()
                    .montoMinimo(1000000L)
                    .montoMaximo(50000000L)
                    .build();

            // When & Then
            assertThat(prestamo.getMontoMinimo()).isLessThanOrEqualTo(prestamo.getMontoMaximo());
        }

        @Test
        @DisplayName("Should handle zero and negative values")
        void shouldHandleZeroAndNegativeValues() {
            // When
            Prestamo prestamo = Prestamo.builder()
                    .id(-1)
                    .nombre("")
                    .montoMinimo(0L)
                    .montoMaximo(-1000L)
                    .tasaInteres(-5.0)
                    .validacionAutomatica(false)
                    .build();

            // Then
            assertThat(prestamo.getId()).isEqualTo(-1);
            assertThat(prestamo.getNombre()).isEqualTo("");
            assertThat(prestamo.getMontoMinimo()).isEqualTo(0L);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(-1000L);
            assertThat(prestamo.getTasaInteres()).isEqualTo(-5.0);
            assertThat(prestamo.getValidacionAutomatica()).isEqualTo(false);
        }

        @Test
        @DisplayName("Should handle null values correctly")
        void shouldHandleNullValuesCorrectly() {
            // When
            Prestamo prestamo = Prestamo.builder()
                    .id(1)
                    .nombre(null)
                    .montoMinimo(1000000L)
                    .montoMaximo(50000000L)
                    .tasaInteres(12.5)
                    .validacionAutomatica(null)
                    .build();

            // Then
            assertThat(prestamo.getId()).isEqualTo(1);
            assertThat(prestamo.getNombre()).isNull();
            assertThat(prestamo.getMontoMinimo()).isEqualTo(1000000L);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(50000000L);
            assertThat(prestamo.getTasaInteres()).isEqualTo(12.5);
            assertThat(prestamo.getValidacionAutomatica()).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very large numbers")
        void shouldHandleVeryLargeNumbers() {
            // Given
            long veryLargeMonto = Long.MAX_VALUE;
            double veryLargeTasa = Double.MAX_VALUE;

            // When
            Prestamo prestamo = Prestamo.builder()
                    .montoMinimo(veryLargeMonto)
                    .montoMaximo(veryLargeMonto)
                    .tasaInteres(veryLargeTasa)
                    .build();

            // Then
            assertThat(prestamo.getMontoMinimo()).isEqualTo(veryLargeMonto);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(veryLargeMonto);
            assertThat(prestamo.getTasaInteres()).isEqualTo(veryLargeTasa);
        }

        @Test
        @DisplayName("Should handle very small numbers")
        void shouldHandleVerySmallNumbers() {
            // Given
            long verySmallMonto = Long.MIN_VALUE;
            double verySmallTasa = Double.MIN_VALUE;

            // When
            Prestamo prestamo = Prestamo.builder()
                    .montoMinimo(verySmallMonto)
                    .montoMaximo(verySmallMonto)
                    .tasaInteres(verySmallTasa)
                    .build();

            // Then
            assertThat(prestamo.getMontoMinimo()).isEqualTo(verySmallMonto);
            assertThat(prestamo.getMontoMaximo()).isEqualTo(verySmallMonto);
            assertThat(prestamo.getTasaInteres()).isEqualTo(verySmallTasa);
        }
    }
}
