package co.com.camilo.r2dbc.entity;

import lombok.*;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table("users")
public class UserEntity {

    @Id
    @Column("correo_electronico")
    private String correo_electronico;
    private String nombre;
    private String apellido;
    private Date fecha_nacimiento;
    private String direccion;
    private String telefono;
    private int salario_base;

}
