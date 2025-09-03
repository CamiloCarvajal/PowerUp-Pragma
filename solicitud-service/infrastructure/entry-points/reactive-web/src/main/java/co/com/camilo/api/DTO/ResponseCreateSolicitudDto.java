package co.com.camilo.api.DTO;


public record ResponseCreateSolicitudDto(
        Integer id,
        Long monto,
        int plazo,
        String email,
        String estado,
        String tipoPrestamo,
        String mensaje
) {
}
