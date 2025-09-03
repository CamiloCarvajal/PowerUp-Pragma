package co.com.camilo.model.exceptions;

public class PrestamoNotFoundException extends BusinessException {

    public PrestamoNotFoundException(int prestamoId) {
        super("El tipo de préstamo con ID " + prestamoId + " no existe", "PRESTAMO_NOT_FOUND");
    }
}
