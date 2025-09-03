package co.com.camilo.usecase.solicitud;

import co.com.camilo.model.solicitud.*;
//import co.com.camilo.model.solicitud.gateways.EstadoRepository;
import co.com.camilo.model.solicitud.gateways.PrestamoRepository;
import co.com.camilo.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final PrestamoRepository tipoPrestamoRepository;

    private static final int ESTADO_PENDIENTE = 1;
    
    public Mono<Solicitud> crearSolicitud(Solicitud solicitud) {

        System.out.println(">> REQUEST3 {}" + solicitud.getPlazo());
        System.out.println(solicitud.getPrestamo().getId());

        return validarTipoPrestamo(solicitud)
                .flatMap(estado -> crearSolicitudConEstado(solicitud))
                .flatMap(solicitudRepository::save);
//                .flatMap(this::construirRespuesta);
    }
    
    private Mono<Solicitud> validarTipoPrestamo(Solicitud solicitud) {
        return tipoPrestamoRepository.findById(solicitud.getPrestamo().getId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El tipo de préstamo especificado no existe")))
                .thenReturn(solicitud);
    }
    
//    private Mono<SolicitudRequest> validarMontoYPlazo(SolicitudRequest request) {
//        return tipoPrestamoRepository.findById(request.getIdTipoPrestamo())
//                .flatMap(tipo -> {
//                    if (request.getMonto().compareTo(tipo.getMontoMinimo()) < 0) {
//                        return Mono.error(new IllegalArgumentException(
//                            String.format("El monto mínimo para este tipo de préstamo es: %s", tipo.getMontoMinimo())));
//                    }
//
//                    if (request.getMonto().compareTo(tipo.getMontoMaximo()) > 0) {
//                        return Mono.error(new IllegalArgumentException(
//                            String.format("El monto máximo para este tipo de préstamo es: %s", tipo.getMontoMaximo())));
//                    }
//
//                    return Mono.just(request);
//                });
//    }
//
//    private Mono<Estado> obtenerEstadoPendiente(SolicitudRequest request) {
//        return estadoRepository.findByNombre(ESTADO_PENDIENTE)
//                .switchIfEmpty(Mono.error(new IllegalStateException("No se pudo obtener el estado pendiente de revisión")));
//    }
    
    private Mono<Solicitud> crearSolicitudConEstado(Solicitud request) {
        System.out.println(">> REQUEST1 {}");
        System.out.println(request);

        return tipoPrestamoRepository.findById(request.getPrestamo().getId())
                .map(tipoPrestamo -> Solicitud.builder()
                        .monto(request.getMonto())
                        .plazo(request.getPlazo())
                        .email(request.getEmail())
                        .estado(Estado.builder().id(ESTADO_PENDIENTE).build())
                        .prestamo(tipoPrestamo)
                        .build());
    }
    
//    private Mono<SolicitudResponse> construirRespuesta(Solicitud solicitud) {
//        return Mono.just(SolicitudResponse.builder()
//                .idSolicitud(solicitud.getIdSolicitud() != null ? solicitud.getIdSolicitud().getValue() : null)
//                .monto(solicitud.getMonto() != null ? solicitud.getMonto().getValue() : null)
//                .plazo(solicitud.getPlazo() != null ? solicitud.getPlazo().getValue() : null)
//                .email(solicitud.getEmail() != null ? solicitud.getEmail().getValue() : null)
//                .estado(solicitud.getEstado() != null ? solicitud.getEstado().getNombre() : null)
//                .tipoPrestamo(solicitud.getTipoPrestamo() != null ? solicitud.getTipoPrestamo().getNombre() : null)
//                .fechaCreacion(solicitud.getFechaCreacion())
//                .mensaje("Solicitud creada exitosamente")
//                .build());
//    }
}
