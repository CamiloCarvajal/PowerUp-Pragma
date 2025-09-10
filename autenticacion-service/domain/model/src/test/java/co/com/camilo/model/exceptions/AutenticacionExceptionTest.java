package co.com.camilo.model.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AutenticacionException Tests")
class AutenticacionExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            String message = "Error de autenticación";
            AutenticacionException exception = new AutenticacionException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            String message = "Error de autenticación";
            RuntimeException cause = new RuntimeException("Causa original");
            AutenticacionException exception = new AutenticacionException(message, cause);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            AutenticacionException exception = new AutenticacionException(null);

            assertNotNull(exception);
            assertNull(exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with null message and cause")
        void shouldCreateExceptionWithNullMessageAndCause() {
            RuntimeException cause = new RuntimeException("Causa original");
            AutenticacionException exception = new AutenticacionException(null, cause);

            assertNotNull(exception);
            assertNull(exception.getMessage());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with empty message")
        void shouldCreateExceptionWithEmptyMessage() {
            String message = "";
            AutenticacionException exception = new AutenticacionException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with blank message")
        void shouldCreateExceptionWithBlankMessage() {
            String message = "   ";
            AutenticacionException exception = new AutenticacionException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {
        
        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            AutenticacionException exception = new AutenticacionException("Test message");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            AutenticacionException exception = new AutenticacionException("Test message");
            
            assertTrue(exception instanceof Throwable);
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {
        
        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            String message = "Error de autenticación";
            AutenticacionException exception = new AutenticacionException(message);

            assertThrows(AutenticacionException.class, () -> {
                throw exception;
            });
        }

        @Test
        @DisplayName("Should preserve cause chain")
        void shouldPreserveCauseChain() {
            String message = "Error de autenticación";
            RuntimeException originalCause = new RuntimeException("Error original");
            AutenticacionException exception = new AutenticacionException(message, originalCause);

            assertEquals(originalCause, exception.getCause());
            assertEquals("Error original", exception.getCause().getMessage());
        }

        @Test
        @DisplayName("Should handle nested exceptions")
        void shouldHandleNestedExceptions() {
            String message = "Error de autenticación";
            RuntimeException nestedCause = new RuntimeException("Error anidado");
            RuntimeException originalCause = new RuntimeException("Error original", nestedCause);
            AutenticacionException exception = new AutenticacionException(message, originalCause);

            assertEquals(originalCause, exception.getCause());
            assertEquals(nestedCause, exception.getCause().getCause());
        }
    }

    @Nested
    @DisplayName("Message Format Tests")
    class MessageFormatTests {
        
        @Test
        @DisplayName("Should handle long messages")
        void shouldHandleLongMessages() {
            String longMessage = "Este es un mensaje de error muy largo que contiene mucha información detallada sobre el problema de autenticación que ha ocurrido en el sistema";
            AutenticacionException exception = new AutenticacionException(longMessage);

            assertEquals(longMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should handle messages with special characters")
        void shouldHandleMessagesWithSpecialCharacters() {
            String specialMessage = "Error: usuario@email.com no pudo autenticarse (código: 401)";
            AutenticacionException exception = new AutenticacionException(specialMessage);

            assertEquals(specialMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should handle messages with newlines")
        void shouldHandleMessagesWithNewlines() {
            String multilineMessage = "Error de autenticación:\nUsuario no encontrado\nToken expirado";
            AutenticacionException exception = new AutenticacionException(multilineMessage);

            assertEquals(multilineMessage, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Stack Trace Tests")
    class StackTraceTests {
        
        @Test
        @DisplayName("Should have stack trace")
        void shouldHaveStackTrace() {
            AutenticacionException exception = new AutenticacionException("Test message");
            StackTraceElement[] stackTrace = exception.getStackTrace();

            assertNotNull(stackTrace);
            assertTrue(stackTrace.length > 0);
        }

        @Test
        @DisplayName("Should preserve cause stack trace")
        void shouldPreserveCauseStackTrace() {
            RuntimeException cause = new RuntimeException("Original error");
            AutenticacionException exception = new AutenticacionException("Wrapper error", cause);

            StackTraceElement[] causeStackTrace = exception.getCause().getStackTrace();
            assertNotNull(causeStackTrace);
            assertTrue(causeStackTrace.length > 0);
        }
    }
}

