package co.com.camilo.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder()
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
