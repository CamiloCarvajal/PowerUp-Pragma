package co.com.camilo.model.solicitud.gateways;

import co.com.camilo.model.solicitud.Prestamo;
import reactor.core.publisher.Mono;

public interface PrestamoRepository {
    Mono<Prestamo> findById(int id);
}
