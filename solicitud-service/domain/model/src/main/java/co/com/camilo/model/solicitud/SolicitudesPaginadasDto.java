package co.com.camilo.model.solicitud;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudesPaginadasDto {
    private List<SolicitudConsultaDto> solicitudes;
    private Long totalElementos;
    private Integer totalPaginas;
    private Integer paginaActual;
    private Integer tamanoPagina;
    private Long deudaTotalMensualSolicitudesAprobadas;
}
