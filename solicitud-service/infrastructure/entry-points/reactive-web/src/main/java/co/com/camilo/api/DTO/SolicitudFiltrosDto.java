package co.com.camilo.api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros para consultar solicitudes")
public record SolicitudFiltrosDto(
        @Schema(description = "Filtrar por plazo en meses", example = "12")
        Integer plazo,
        
        @Schema(description = "Filtrar por email del solicitante", example = "usuario@email.com")
        String email,
        
        @Schema(description = "Filtrar por nombre del solicitante", example = "Juan Pérez")
        String nombre,
        
        @Schema(description = "Filtrar por ID del tipo de préstamo", example = "1")
        Integer tipoPrestamo,
        
        @Schema(description = "Filtrar por ID del estado de la solicitud", example = "1")
        Integer estadoSolicitud,
        
        @Schema(description = "Número de página (base 0)", example = "0")
        Integer pagina,
        
        @Schema(description = "Tamaño de página", example = "10")
        Integer tamano
) {
}
