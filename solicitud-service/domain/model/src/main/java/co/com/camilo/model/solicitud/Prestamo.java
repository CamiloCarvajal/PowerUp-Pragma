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
public class Prestamo {

    private int id;
    private String nombre;
    private long montoMinimo;
    private long montoMaximo;
    private double tasaInteres;
    private Boolean validacionAutomatica;
}
