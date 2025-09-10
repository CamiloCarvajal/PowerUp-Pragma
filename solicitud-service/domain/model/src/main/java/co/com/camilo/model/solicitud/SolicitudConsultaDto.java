package co.com.camilo.model.solicitud;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudConsultaDto {
    private Long monto;
    private Integer plazo;
    private String email;
    private String nombre;
    private Integer tipoPrestamo;
    private Double tasaInteres;
    private Integer estadoSolicitud;
    private Long salarioBase;
}
