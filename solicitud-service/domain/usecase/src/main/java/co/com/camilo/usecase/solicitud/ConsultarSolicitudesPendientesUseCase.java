package co.com.camilo.usecase.solicitud;

import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import co.com.camilo.model.exceptions.AccesoDenegadoException;
import co.com.camilo.model.solicitud.Solicitud;
import co.com.camilo.model.solicitud.SolicitudConsultaDto;
import co.com.camilo.model.solicitud.SolicitudesPaginadasDto;
import co.com.camilo.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ConsultarSolicitudesPendientesUseCase {

    private final SolicitudRepository solicitudRepository;

    public Mono<SolicitudesPaginadasDto> consultarSolicitudesPendientes(
            Integer plazo,
            String email,
            String nombre,
            Integer tipoPrestamo,
            Integer estadoSolicitud,
            int pagina,
            int tamano,
            UsuarioAutenticado usuario
    ) {
        return validarUsuarioAsesor(usuario)
                .then(consultarSolicitudesConFiltros(plazo, email, nombre, tipoPrestamo, estadoSolicitud, pagina, tamano))
                .flatMap(resultado -> consultarDeudaTotalAprobadas()
                        .map(deudaTotal -> construirRespuesta(resultado.solicitudes(), resultado.total(), pagina, tamano, deudaTotal)));
    }

    private Mono<Void> validarUsuarioAsesor(UsuarioAutenticado usuario) {
        if (usuario == null) {
            return Mono.error(new AccesoDenegadoException("Usuario no autenticado"));
        }

        if (!"ASESOR".equalsIgnoreCase(usuario.getNombreRol())) {
            return Mono.error(new AccesoDenegadoException("Solo los usuarios tipo ASESOR pueden consultar solicitudes pendientes"));
        }

        return Mono.empty();
    }

    private Mono<ResultadoConsulta> consultarSolicitudesConFiltros(
            Integer plazo,
            String email,
            String nombre,
            Integer tipoPrestamo,
            Integer estadoSolicitud,
            int pagina,
            int tamano
    ) {
        return Mono.zip(
                solicitudRepository.findSolicitudesPendientesConFiltros(plazo, email, nombre, tipoPrestamo, estadoSolicitud, pagina, tamano)
                        .map(this::mapToResponseDto)
                        .collectList(),
                solicitudRepository.countSolicitudesPendientesConFiltros(plazo, email, nombre, tipoPrestamo, estadoSolicitud)
        ).map(tuple -> new ResultadoConsulta(tuple.getT1(), tuple.getT2()));
    }

    private Mono<Long> consultarDeudaTotalAprobadas() {
        return solicitudRepository.sumMontoSolicitudesAprobadas();
    }

    private SolicitudConsultaDto mapToResponseDto(Solicitud solicitud) {
        return SolicitudConsultaDto.builder()
                .monto(solicitud.getMonto())
                .plazo(solicitud.getPlazo())
                .email(solicitud.getEmail())
                .estado(solicitud.getEstado() != null ? solicitud.getEstado().getNombre() : null)
                .tipoPrestamo(solicitud.getPrestamo() != null ? solicitud.getPrestamo().getNombre() : null)
                .tasaInteres(solicitud.getPrestamo() != null ? solicitud.getPrestamo().getTasaInteres() : null)
                .estadoSolicitud(solicitud.getEstado() != null ? solicitud.getEstado().getId() : null)
                .salarioBase(solicitud.getSalarioBase())
                .build();
    }

    private SolicitudesPaginadasDto construirRespuesta(
            List<SolicitudConsultaDto> solicitudes,
            Long total,
            int pagina,
            int tamano,
            Long deudaTotal
    ) {
        int totalPaginas = (int) Math.ceil((double) total / tamano);

        return SolicitudesPaginadasDto.builder()
                .solicitudes(solicitudes)
                .totalElementos(total)
                .totalPaginas(totalPaginas)
                .paginaActual(pagina)
                .tamanoPagina(tamano)
                .deudaTotalMensualSolicitudesAprobadas(deudaTotal)
                .build();
    }

    private record ResultadoConsulta(List<SolicitudConsultaDto> solicitudes, Long total) {
    }
}
