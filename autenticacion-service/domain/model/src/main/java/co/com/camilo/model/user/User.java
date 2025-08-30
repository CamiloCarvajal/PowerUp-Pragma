package co.com.camilo.model.user;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    private int id;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private int salarioBase;

}
