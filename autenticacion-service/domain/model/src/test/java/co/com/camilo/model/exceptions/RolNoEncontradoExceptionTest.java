package co.com.camilo.model.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RolNoEncontradoException Tests")
class RolNoEncontradoExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            String message = "Rol no encontrado";
            RolNoEncontradoException exception = new RolNoEncontradoException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            String message = "Rol no encontrado";
            RuntimeException cause = new RuntimeException("Causa original");
            RolNoEncontradoException exception = new RolNoEncontradoException(message, cause);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            RolNoEncontradoException exception = new RolNoEncontradoException(null);

            assertNotNull(exception);
            assertNull(exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with empty message")
        void shouldCreateExceptionWithEmptyMessage() {
            String message = "";
            RolNoEncontradoException exception = new RolNoEncontradoException(message);

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
            RolNoEncontradoException exception = new RolNoEncontradoException("Test message");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            RolNoEncontradoException exception = new RolNoEncontradoException("Test message");
            
            assertTrue(exception instanceof Throwable);
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {
        
        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            String message = "Rol no encontrado";
            RolNoEncontradoException exception = new RolNoEncontradoException(message);

            assertThrows(RolNoEncontradoException.class, () -> {
                throw exception;
            });
        }

        @Test
        @DisplayName("Should preserve cause chain")
        void shouldPreserveCauseChain() {
            String message = "Rol no encontrado";
            RuntimeException originalCause = new RuntimeException("Error original");
            RolNoEncontradoException exception = new RolNoEncontradoException(message, originalCause);

            assertEquals(originalCause, exception.getCause());
            assertEquals("Error original", exception.getCause().getMessage());
        }
    }
}

