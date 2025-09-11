package co.com.camilo.model.exceptions;

public class SolicitudNotFoundException extends RuntimeException {
    
    public SolicitudNotFoundException(String mensaje) {
        super(mensaje);
    }
    
    public SolicitudNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
