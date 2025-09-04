package co.com.camilo.model.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PrestamoNotFoundException Tests")
class PrestamoNotFoundExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create PrestamoNotFoundException with valid prestamo ID")
        void shouldCreatePrestamoNotFoundExceptionWithValidPrestamoId() {
            // Given
            int prestamoId = 1;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("El tipo de préstamo con ID 1 no existe");
            assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
        }

        @Test
        @DisplayName("Should create PrestamoNotFoundException with zero prestamo ID")
        void shouldCreatePrestamoNotFoundExceptionWithZeroPrestamoId() {
            // Given
            int prestamoId = 0;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("El tipo de préstamo con ID 0 no existe");
            assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
        }

        @Test
        @DisplayName("Should create PrestamoNotFoundException with negative prestamo ID")
        void shouldCreatePrestamoNotFoundExceptionWithNegativePrestamoId() {
            // Given
            int prestamoId = -1;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("El tipo de préstamo con ID -1 no existe");
            assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
        }

        @Test
        @DisplayName("Should create PrestamoNotFoundException with large prestamo ID")
        void shouldCreatePrestamoNotFoundExceptionWithLargePrestamoId() {
            // Given
            int prestamoId = Integer.MAX_VALUE;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("El tipo de préstamo con ID " + Integer.MAX_VALUE + " no existe");
            assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should be instance of BusinessException")
        void shouldBeInstanceOfBusinessException() {
            // Given
            int prestamoId = 1;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception).isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Given
            int prestamoId = 1;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Error Code Tests")
    class ErrorCodeTests {

        @Test
        @DisplayName("Should have correct error code")
        void shouldHaveCorrectErrorCode() {
            // Given
            int prestamoId = 1;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
        }

        @Test
        @DisplayName("Should have consistent error code for different IDs")
        void shouldHaveConsistentErrorCodeForDifferentIds() {
            // Given
            int[] prestamoIds = {1, 0, -1, 999, Integer.MAX_VALUE, Integer.MIN_VALUE};

            for (int prestamoId : prestamoIds) {
                // When
                PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

                // Then
                assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
            }
        }
    }

    @Nested
    @DisplayName("Message Format Tests")
    class MessageFormatTests {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 10, 100, 1000, 9999})
        @DisplayName("Should format message correctly for positive IDs")
        void shouldFormatMessageCorrectlyForPositiveIds(int prestamoId) {
            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            String expectedMessage = "El tipo de préstamo con ID " + prestamoId + " no existe";
            assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, -100, -1000, Integer.MIN_VALUE})
        @DisplayName("Should format message correctly for zero and negative IDs")
        void shouldFormatMessageCorrectlyForZeroAndNegativeIds(int prestamoId) {
            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            String expectedMessage = "El tipo de préstamo con ID " + prestamoId + " no existe";
            assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("Should format message correctly for maximum integer value")
        void shouldFormatMessageCorrectlyForMaximumIntegerValue() {
            // Given
            int prestamoId = Integer.MAX_VALUE;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            String expectedMessage = "El tipo de préstamo con ID " + Integer.MAX_VALUE + " no existe";
            assertThat(exception.getMessage()).isEqualTo(expectedMessage);
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable and catchable")
        void shouldBeThrowableAndCatchable() {
            // Given
            int prestamoId = 1;

            // When & Then
            assertThatThrownBy(() -> {
                throw new PrestamoNotFoundException(prestamoId);
            })
            .isInstanceOf(PrestamoNotFoundException.class)
            .isInstanceOf(BusinessException.class)
            .hasMessage("El tipo de préstamo con ID 1 no existe");
        }

        @Test
        @DisplayName("Should maintain properties when thrown and caught")
        void shouldMaintainPropertiesWhenThrownAndCaught() {
            // Given
            int prestamoId = 42;

            // When
            PrestamoNotFoundException caughtException = null;
            try {
                throw new PrestamoNotFoundException(prestamoId);
            } catch (PrestamoNotFoundException e) {
                caughtException = e;
            }

            // Then
            assertThat(caughtException).isNotNull();
            assertThat(caughtException.getMessage()).isEqualTo("El tipo de préstamo con ID 42 no existe");
            assertThat(caughtException.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
        }

        @Test
        @DisplayName("Should be catchable as BusinessException")
        void shouldBeCatchableAsBusinessException() {
            // Given
            int prestamoId = 1;

            // When
            BusinessException caughtException = null;
            try {
                throw new PrestamoNotFoundException(prestamoId);
            } catch (BusinessException e) {
                caughtException = e;
            }

            // Then
            assertThat(caughtException).isNotNull();
            assertThat(caughtException).isInstanceOf(PrestamoNotFoundException.class);
            assertThat(caughtException.getMessage()).isEqualTo("El tipo de préstamo con ID 1 no existe");
            assertThat(caughtException.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
        }
    }

    @Nested
    @DisplayName("Business Context Tests")
    class BusinessContextTests {

        @Test
        @DisplayName("Should represent business rule violation")
        void shouldRepresentBusinessRuleViolation() {
            // Given
            int prestamoId = 1;

            // When
            PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
            assertThat(exception.getMessage()).contains("no existe");
            assertThat(exception.getMessage()).contains(String.valueOf(prestamoId));
        }

        @Test
        @DisplayName("Should be suitable for business logic validation")
        void shouldBeSuitableForBusinessLogicValidation() {
            // Given
            int[] invalidPrestamoIds = {0, -1, 999, Integer.MAX_VALUE};

            for (int prestamoId : invalidPrestamoIds) {
                // When
                PrestamoNotFoundException exception = new PrestamoNotFoundException(prestamoId);

                // Then
                assertThat(exception.getErrorCode()).isEqualTo("PRESTAMO_NOT_FOUND");
                assertThat(exception.getMessage()).isNotNull();
                assertThat(exception.getMessage()).isNotEmpty();
            }
        }
    }
}
