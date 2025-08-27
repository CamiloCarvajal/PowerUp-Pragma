package co.com.camilo.api.DTO;

import lombok.Data;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class CreateUserRequest {

    @Schema(description = "Nombre del usuario", example = "Juan", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez", required = true)
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario", example = "camilo@email.com", required = true)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String correoElectronico;

    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-01-01", required = true)
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @Schema(description = "Dirección del usuario", example = "Calle 123 #45-67")
    private String direccion;

    @Schema(description = "Teléfono del usuario", example = "+57 300 123 4567")
    private String telefono;

    @Schema(description = "Salario base del usuario", example = "50000", required = true)
    @NotNull(message = "El salario base no debe estar vacío")
    @Min(value = 0, message = "El salario base debe ser mayor o igual a 0")
    @Max(value = 15000000, message = "El salario base debe ser menor o igual a 15000000")
    private int salarioBase;
}
