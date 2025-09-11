package co.com.camilo.api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información de una solicitud de préstamo")
public record SolicitudResponseDto(
        @Schema(description = "Monto de la solicitud", example = "5000000")
        Long monto,
        
        @Schema(description = "Plazo en meses", example = "12")
        Integer plazo,
        
        @Schema(description = "Email del solicitante", example = "usuario@email.com")
        String email,
        
//        @Schema(description = "Nombre del solicitante", example = "Juan Pérez")
//        String nombre,

        @Schema(description = "Estado", example = "Juan Pérez")
        String estado,
        
        @Schema(description = "Tipo de préstamo", example = "1")
        String tipoPrestamo,
        
        @Schema(description = "Tasa de interés", example = "12.5")
        Double tasaInteres,
        
        @Schema(description = "ID del estado de la solicitud", example = "1")
        Integer estadoSolicitud
        
//        @Schema(description = "Salario base del solicitante", example = "3000000")
//        Long salarioBase
) {
}
