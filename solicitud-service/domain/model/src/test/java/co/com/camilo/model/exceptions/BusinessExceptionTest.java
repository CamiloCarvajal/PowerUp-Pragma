package co.com.camilo.model.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BusinessException Tests")
class BusinessExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create BusinessException with message and error code")
        void shouldCreateBusinessExceptionWithMessageAndErrorCode() {
            // Given
            String message = "Test business error message";
            String errorCode = "TEST_ERROR_CODE";

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        }

        @Test
        @DisplayName("Should create BusinessException with null message and error code")
        void shouldCreateBusinessExceptionWithNullMessageAndErrorCode() {
            // Given
            String message = null;
            String errorCode = "NULL_MESSAGE_ERROR";

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isNull();
            assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        }

        @Test
        @DisplayName("Should create BusinessException with empty message and error code")
        void shouldCreateBusinessExceptionWithEmptyMessageAndErrorCode() {
            // Given
            String message = "";
            String errorCode = "EMPTY_MESSAGE_ERROR";

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("");
            assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        }

        @Test
        @DisplayName("Should create BusinessException with null error code")
        void shouldCreateBusinessExceptionWithNullErrorCode() {
            // Given
            String message = "Test message";
            String errorCode = null;

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getErrorCode()).isNull();
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Given
            String message = "Test message";
            String errorCode = "TEST_ERROR";

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should be instance of BusinessException")
        void shouldBeInstanceOfBusinessException() {
            // Given
            String message = "Test message";
            String errorCode = "TEST_ERROR";

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception).isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    @DisplayName("Error Code Tests")
    class ErrorCodeTests {

        @Test
        @DisplayName("Should return correct error code")
        void shouldReturnCorrectErrorCode() {
            // Given
            String message = "Test message";
            String errorCode = "SPECIFIC_ERROR_CODE";

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        }

        @Test
        @DisplayName("Should handle different error code formats")
        void shouldHandleDifferentErrorCodeFormats() {
            // Test cases for different error code formats
            String[] errorCodes = {
                "SIMPLE_ERROR",
                "ERROR_WITH_UNDERSCORES",
                "error-with-dashes",
                "ErrorWithCamelCase",
                "ERROR123",
                "error_with_numbers_123"
            };

            for (String errorCode : errorCodes) {
                // When
                BusinessException exception = new TestBusinessException("Test", errorCode);

                // Then
                assertThat(exception.getErrorCode()).isEqualTo(errorCode);
            }
        }
    }

    @Nested
    @DisplayName("Message Tests")
    class MessageTests {

        @Test
        @DisplayName("Should return correct message")
        void shouldReturnCorrectMessage() {
            // Given
            String message = "This is a test business error message";
            String errorCode = "TEST_ERROR";

            // When
            BusinessException exception = new TestBusinessException(message, errorCode);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should handle long messages")
        void shouldHandleLongMessages() {
            // Given
            String longMessage = "A".repeat(1000);
            String errorCode = "LONG_MESSAGE_ERROR";

            // When
            BusinessException exception = new TestBusinessException(longMessage, errorCode);

            // Then
            assertThat(exception.getMessage()).isEqualTo(longMessage);
        }

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            // Given
            String messageWithSpecialChars = "Error with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
            String errorCode = "SPECIAL_CHARS_ERROR";

            // When
            BusinessException exception = new TestBusinessException(messageWithSpecialChars, errorCode);

            // Then
            assertThat(exception.getMessage()).isEqualTo(messageWithSpecialChars);
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable and catchable")
        void shouldBeThrowableAndCatchable() {
            // Given
            String message = "Test throwable exception";
            String errorCode = "THROWABLE_ERROR";

            // When & Then
            assertThatThrownBy(() -> {
                throw new TestBusinessException(message, errorCode);
            })
            .isInstanceOf(BusinessException.class)
            .hasMessage(message);
        }

        @Test
        @DisplayName("Should maintain error code when thrown and caught")
        void shouldMaintainErrorCodeWhenThrownAndCaught() {
            // Given
            String message = "Test error code preservation";
            String errorCode = "PRESERVED_ERROR_CODE";

            // When
            BusinessException caughtException = null;
            try {
                throw new TestBusinessException(message, errorCode);
            } catch (BusinessException e) {
                caughtException = e;
            }

            // Then
            assertThat(caughtException).isNotNull();
            assertThat(caughtException.getMessage()).isEqualTo(message);
            assertThat(caughtException.getErrorCode()).isEqualTo(errorCode);
        }
    }

    // Helper class for testing BusinessException
    private static class TestBusinessException extends BusinessException {
        public TestBusinessException(String message, String errorCode) {
            super(message, errorCode);
        }
    }
}
