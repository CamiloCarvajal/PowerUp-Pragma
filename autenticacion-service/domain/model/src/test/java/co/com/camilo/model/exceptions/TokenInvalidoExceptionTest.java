package co.com.camilo.model.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TokenInvalidoException Tests")
class TokenInvalidoExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            String message = "Token inválido";
            TokenInvalidoException exception = new TokenInvalidoException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            String message = "Token inválido";
            RuntimeException cause = new RuntimeException("Causa original");
            TokenInvalidoException exception = new TokenInvalidoException(message, cause);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            TokenInvalidoException exception = new TokenInvalidoException(null);

            assertNotNull(exception);
            assertNull(exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with empty message")
        void shouldCreateExceptionWithEmptyMessage() {
            String message = "";
            TokenInvalidoException exception = new TokenInvalidoException(message);

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
            TokenInvalidoException exception = new TokenInvalidoException("Test message");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            TokenInvalidoException exception = new TokenInvalidoException("Test message");
            
            assertTrue(exception instanceof Throwable);
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {
        
        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            String message = "Token inválido";
            TokenInvalidoException exception = new TokenInvalidoException(message);

            assertThrows(TokenInvalidoException.class, () -> {
                throw exception;
            });
        }

        @Test
        @DisplayName("Should preserve cause chain")
        void shouldPreserveCauseChain() {
            String message = "Token inválido";
            RuntimeException originalCause = new RuntimeException("Error original");
            TokenInvalidoException exception = new TokenInvalidoException(message, originalCause);

            assertEquals(originalCause, exception.getCause());
            assertEquals("Error original", exception.getCause().getMessage());
        }
    }
}

