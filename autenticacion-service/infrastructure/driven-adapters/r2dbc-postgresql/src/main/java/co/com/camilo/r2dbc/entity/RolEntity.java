package co.com.camilo.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table("roles")
public class RolEntity {

    @Id
    private int id;
    private String nombre;
    private String descripcion;

}

