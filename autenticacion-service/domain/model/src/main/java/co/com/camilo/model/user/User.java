package co.com.camilo.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String nombre;
    private String apellido;
    private String correo_electronico;
    private Date fecha_nacimiento;
    private String direccion;
    private String telefono;
    private int salario_base;

}
