package co.com.camilo.model.solicitud.gateways;

import co.com.camilo.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Solicitud> save(Solicitud solicitud);
    Mono<Solicitud> findById(int id);
}
