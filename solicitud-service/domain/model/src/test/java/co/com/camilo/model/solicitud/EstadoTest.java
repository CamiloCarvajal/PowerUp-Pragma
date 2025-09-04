package co.com.camilo.model.solicitud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Estado Domain Entity Tests")
class EstadoTest {

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create Estado with all fields using builder")
        void shouldCreateEstadoWithAllFieldsUsingBuilder() {
            // Given
            int id = 1;
            String nombre = "Pendiente";
            String descripcion = "Solicitud pendiente de revisión";

            // When
            Estado estado = Estado.builder()
                    .id(id)
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .build();

            // Then
            assertThat(estado).isNotNull();
            assertThat(estado.getId()).isEqualTo(id);
            assertThat(estado.getNombre()).isEqualTo(nombre);
            assertThat(estado.getDescripcion()).isEqualTo(descripcion);
        }

        @Test
        @DisplayName("Should create Estado with minimal fields using builder")
        void shouldCreateEstadoWithMinimalFieldsUsingBuilder() {
            // When
            Estado estado = Estado.builder()
                    .nombre("Aprobado")
                    .build();

            // Then
            assertThat(estado).isNotNull();
            assertThat(estado.getId()).isEqualTo(0);
            assertThat(estado.getNombre()).isEqualTo("Aprobado");
            assertThat(estado.getDescripcion()).isNull();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create Estado with no-args constructor")
        void shouldCreateEstadoWithNoArgsConstructor() {
            // When
            Estado estado = new Estado();

            // Then
            assertThat(estado).isNotNull();
            assertThat(estado.getId()).isEqualTo(0);
            assertThat(estado.getNombre()).isNull();
            assertThat(estado.getDescripcion()).isNull();
        }

        @Test
        @DisplayName("Should create Estado with all-args constructor")
        void shouldCreateEstadoWithAllArgsConstructor() {
            // Given
            int id = 1;
            String nombre = "Pendiente";
            String descripcion = "Solicitud pendiente de revisión";

            // When
            Estado estado = new Estado(id, nombre, descripcion);

            // Then
            assertThat(estado).isNotNull();
            assertThat(estado.getId()).isEqualTo(id);
            assertThat(estado.getNombre()).isEqualTo(nombre);
            assertThat(estado.getDescripcion()).isEqualTo(descripcion);
        }
    }

    @Nested
    @DisplayName("Setters and Getters Tests")
    class SettersAndGettersTests {

        @Test
        @DisplayName("Should set and get all fields correctly")
        void shouldSetAndGetAllFieldsCorrectly() {
            // Given
            Estado estado = new Estado();
            int id = 1;
            String nombre = "Pendiente";
            String descripcion = "Solicitud pendiente de revisión";

            // When
            estado.setId(id);
            estado.setNombre(nombre);
            estado.setDescripcion(descripcion);

            // Then
            assertThat(estado.getId()).isEqualTo(id);
            assertThat(estado.getNombre()).isEqualTo(nombre);
            assertThat(estado.getDescripcion()).isEqualTo(descripcion);
        }
    }

    @Nested
    @DisplayName("ToBuilder Tests")
    class ToBuilderTests {

        @Test
        @DisplayName("Should create new instance using toBuilder")
        void shouldCreateNewInstanceUsingToBuilder() {
            // Given
            Estado originalEstado = Estado.builder()
                    .id(1)
                    .nombre("Pendiente")
                    .descripcion("Solicitud pendiente de revisión")
                    .build();

            // When
            Estado modifiedEstado = originalEstado.toBuilder()
                    .nombre("Aprobado")
                    .descripcion("Solicitud aprobada")
                    .build();

            // Then
            assertThat(modifiedEstado).isNotNull();
            assertThat(modifiedEstado.getId()).isEqualTo(1);
            assertThat(modifiedEstado.getNombre()).isEqualTo("Aprobado");
            assertThat(modifiedEstado.getDescripcion()).isEqualTo("Solicitud aprobada");

            // Original should remain unchanged
            assertThat(originalEstado.getNombre()).isEqualTo("Pendiente");
            assertThat(originalEstado.getDescripcion()).isEqualTo("Solicitud pendiente de revisión");
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should handle different estado types")
        void shouldHandleDifferentEstadoTypes() {
            // Given & When
            Estado pendiente = Estado.builder()
                    .id(1)
                    .nombre("Pendiente")
                    .descripcion("Solicitud pendiente de revisión")
                    .build();

            Estado aprobado = Estado.builder()
                    .id(2)
                    .nombre("Aprobado")
                    .descripcion("Solicitud aprobada")
                    .build();

            Estado rechazado = Estado.builder()
                    .id(3)
                    .nombre("Rechazado")
                    .descripcion("Solicitud rechazada")
                    .build();

            // Then
            assertThat(pendiente.getNombre()).isEqualTo("Pendiente");
            assertThat(aprobado.getNombre()).isEqualTo("Aprobado");
            assertThat(rechazado.getNombre()).isEqualTo("Rechazado");

            assertThat(pendiente.getId()).isNotEqualTo(aprobado.getId());
            assertThat(aprobado.getId()).isNotEqualTo(rechazado.getId());
        }

        @Test
        @DisplayName("Should handle empty and null values")
        void shouldHandleEmptyAndNullValues() {
            // When
            Estado estado = Estado.builder()
                    .id(1)
                    .nombre("")
                    .descripcion("")
                    .build();

            Estado estadoNull = Estado.builder()
                    .id(2)
                    .nombre(null)
                    .descripcion(null)
                    .build();

            // Then
            assertThat(estado.getNombre()).isEqualTo("");
            assertThat(estado.getDescripcion()).isEqualTo("");
            assertThat(estadoNull.getNombre()).isNull();
            assertThat(estadoNull.getDescripcion()).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle zero and negative IDs")
        void shouldHandleZeroAndNegativeIDs() {
            // When
            Estado estadoZero = Estado.builder()
                    .id(0)
                    .nombre("Estado Cero")
                    .build();

            Estado estadoNegative = Estado.builder()
                    .id(-1)
                    .nombre("Estado Negativo")
                    .build();

            // Then
            assertThat(estadoZero.getId()).isEqualTo(0);
            assertThat(estadoNegative.getId()).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should handle very large IDs")
        void shouldHandleVeryLargeIDs() {
            // Given
            int veryLargeId = Integer.MAX_VALUE;

            // When
            Estado estado = Estado.builder()
                    .id(veryLargeId)
                    .nombre("Estado Grande")
                    .build();

            // Then
            assertThat(estado.getId()).isEqualTo(veryLargeId);
        }

        @Test
        @DisplayName("Should handle long strings")
        void shouldHandleLongStrings() {
            // Given
            String longNombre = "A".repeat(1000);
            String longDescripcion = "B".repeat(2000);

            // When
            Estado estado = Estado.builder()
                    .id(1)
                    .nombre(longNombre)
                    .descripcion(longDescripcion)
                    .build();

            // Then
            assertThat(estado.getNombre()).isEqualTo(longNombre);
            assertThat(estado.getDescripcion()).isEqualTo(longDescripcion);
        }
    }

    @Nested
    @DisplayName("Common Estado Values Tests")
    class CommonEstadoValuesTests {

        @Test
        @DisplayName("Should create common solicitud estados")
        void shouldCreateCommonSolicitudEstados() {
            // Given & When
            Estado pendiente = Estado.builder()
                    .id(1)
                    .nombre("Pendiente")
                    .descripcion("Solicitud pendiente de revisión")
                    .build();

            Estado enRevision = Estado.builder()
                    .id(2)
                    .nombre("En Revisión")
                    .descripcion("Solicitud siendo revisada por el analista")
                    .build();

            Estado aprobado = Estado.builder()
                    .id(3)
                    .nombre("Aprobado")
                    .descripcion("Solicitud aprobada y lista para desembolso")
                    .build();

            Estado rechazado = Estado.builder()
                    .id(4)
                    .nombre("Rechazado")
                    .descripcion("Solicitud rechazada por no cumplir criterios")
                    .build();

            Estado cancelado = Estado.builder()
                    .id(5)
                    .nombre("Cancelado")
                    .descripcion("Solicitud cancelada por el cliente")
                    .build();

            // Then
            assertThat(pendiente.getNombre()).isEqualTo("Pendiente");
            assertThat(enRevision.getNombre()).isEqualTo("En Revisión");
            assertThat(aprobado.getNombre()).isEqualTo("Aprobado");
            assertThat(rechazado.getNombre()).isEqualTo("Rechazado");
            assertThat(cancelado.getNombre()).isEqualTo("Cancelado");

            // Verify all have different IDs
            assertThat(pendiente.getId()).isNotEqualTo(enRevision.getId());
            assertThat(enRevision.getId()).isNotEqualTo(aprobado.getId());
            assertThat(aprobado.getId()).isNotEqualTo(rechazado.getId());
            assertThat(rechazado.getId()).isNotEqualTo(cancelado.getId());
        }
    }
}
