package co.com.camilo.model.solicitud.gateways;

import co.com.camilo.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Solicitud> save(Solicitud solicitud);
    Mono<Solicitud> findById(int id);
    Flux<Solicitud> findSolicitudesPendientesConFiltros(Integer plazo, String email, String nombre, Integer tipoPrestamo, Integer estadoSolicitud, int pagina, int tamano);
    Mono<Long> countSolicitudesPendientesConFiltros(Integer plazo, String email, String nombre, Integer tipoPrestamo, Integer estadoSolicitud);
    Mono<Long> sumMontoSolicitudesAprobadas();
}
