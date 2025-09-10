package co.com.camilo.model.exceptions;

public class RolNoEncontradoException extends RuntimeException {
    
    public RolNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
    public RolNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

