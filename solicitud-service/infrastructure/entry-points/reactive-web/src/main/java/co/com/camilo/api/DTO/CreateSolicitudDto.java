package co.com.camilo.api.DTO;

import co.com.camilo.model.solicitud.Estado;
import co.com.camilo.model.solicitud.Prestamo;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


public record CreateSolicitudDto(

        @NotNull(message = "Debe ingresar un monto")
        @Min(value = 1, message = "El monto debe ser mayor a cero")
        Long monto,

        @NotNull(message = "Debe ingresar un plazo")
        @Min(value = 1, message = "El plazo debe ser mayor a cero")
        int plazo,

        @NotBlank(message = "Debe ingresar un correo electronico")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El formato del correo no es válido")
        String email,

        @NotNull(message = "Debe ingresar un tipo de prestamo")
        int prestamo) {
}
