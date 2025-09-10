package co.com.camilo.model.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CredencialesInvalidasException Tests")
class CredencialesInvalidasExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            String message = "Credenciales inválidas";
            CredencialesInvalidasException exception = new CredencialesInvalidasException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            String message = "Credenciales inválidas";
            RuntimeException cause = new RuntimeException("Causa original");
            CredencialesInvalidasException exception = new CredencialesInvalidasException(message, cause);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            CredencialesInvalidasException exception = new CredencialesInvalidasException(null);

            assertNotNull(exception);
            assertNull(exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with empty message")
        void shouldCreateExceptionWithEmptyMessage() {
            String message = "";
            CredencialesInvalidasException exception = new CredencialesInvalidasException(message);

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
            CredencialesInvalidasException exception = new CredencialesInvalidasException("Test message");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            CredencialesInvalidasException exception = new CredencialesInvalidasException("Test message");
            
            assertTrue(exception instanceof Throwable);
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {
        
        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            String message = "Credenciales inválidas";
            CredencialesInvalidasException exception = new CredencialesInvalidasException(message);

            assertThrows(CredencialesInvalidasException.class, () -> {
                throw exception;
            });
        }

        @Test
        @DisplayName("Should preserve cause chain")
        void shouldPreserveCauseChain() {
            String message = "Credenciales inválidas";
            RuntimeException originalCause = new RuntimeException("Error original");
            CredencialesInvalidasException exception = new CredencialesInvalidasException(message, originalCause);

            assertEquals(originalCause, exception.getCause());
            assertEquals("Error original", exception.getCause().getMessage());
        }
    }
}

