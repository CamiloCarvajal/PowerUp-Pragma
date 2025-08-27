package co.com.camilo.r2dbc.entity;

import lombok.*;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table("usuarios")
public class UserEntity {

    @Id
    private int id;
    @Column("correo_electronico")
    private String correoElectronico;
    private String nombre;
    private String apellido;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    @Column("salario_base")
    private int salarioBase;

}
