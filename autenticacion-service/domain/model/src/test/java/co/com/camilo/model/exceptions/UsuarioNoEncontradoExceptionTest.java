package co.com.camilo.model.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioNoEncontradoException Tests")
class UsuarioNoEncontradoExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            String message = "Usuario no encontrado";
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            String message = "Usuario no encontrado";
            RuntimeException cause = new RuntimeException("Causa original");
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(message, cause);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(null);

            assertNotNull(exception);
            assertNull(exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with empty message")
        void shouldCreateExceptionWithEmptyMessage() {
            String message = "";
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(message);

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
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException("Test message");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException("Test message");
            
            assertTrue(exception instanceof Throwable);
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {
        
        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            String message = "Usuario no encontrado";
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(message);

            assertThrows(UsuarioNoEncontradoException.class, () -> {
                throw exception;
            });
        }

        @Test
        @DisplayName("Should preserve cause chain")
        void shouldPreserveCauseChain() {
            String message = "Usuario no encontrado";
            RuntimeException originalCause = new RuntimeException("Error original");
            UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(message, originalCause);

            assertEquals(originalCause, exception.getCause());
            assertEquals("Error original", exception.getCause().getMessage());
        }
    }
}

