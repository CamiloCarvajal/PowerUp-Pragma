package co.com.camilo.model.exceptions;

public class AutenticacionException extends RuntimeException {
    
    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
    
    public AutenticacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

