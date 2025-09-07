package co.com.camilo.model.rol;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rol {

    @EqualsAndHashCode.Include
    private int id;
    private String nombre;
    private String descripcion;

}

