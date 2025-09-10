package co.com.camilo.api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Respuesta paginada de solicitudes de préstamo")
public record SolicitudesPaginadasResponseDto(
        @Schema(description = "Lista de solicitudes")
        List<SolicitudResponseDto> solicitudes,
        
        @Schema(description = "Número total de elementos", example = "50")
        Long totalElementos,
        
        @Schema(description = "Número total de páginas", example = "5")
        Integer totalPaginas,
        
        @Schema(description = "Número de página actual", example = "1")
        Integer paginaActual,
        
        @Schema(description = "Tamaño de página", example = "10")
        Integer tamanoPagina,
        
        @Schema(description = "Deuda total mensual de solicitudes aprobadas", example = "15000000")
        Long deudaTotalMensualSolicitudesAprobadas
) {
}
