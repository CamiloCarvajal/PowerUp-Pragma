package co.com.camilo.api.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record CreateSolicitudDto(

        @Schema(description = "Valor del monto a solicitar", example = "5000000", required = true)
        @NotNull(message = "Debe ingresar un monto")
        @Min(value = 1, message = "El monto debe ser mayor a cero")
        Long monto,

        @Schema(description = "Numero de meses para pagar el credito", example = "2", required = true, type = "integer")
        @NotNull(message = "Debe ingresar un plazo")
        @Min(value = 1, message = "El plazo debe ser mayor a cero")
        int plazo,

        @Schema(description = "Correo electronico", example = "camilo@email.com", required = true)
        @NotBlank(message = "Debe ingresar un correo electronico")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El formato del correo no es válido")
        String email,

        @Schema(description = "ID del tipo de prestamo", example = "1", required = true)
        @NotNull(message = "Debe ingresar un tipo de prestamo")
        int prestamo) {
}
