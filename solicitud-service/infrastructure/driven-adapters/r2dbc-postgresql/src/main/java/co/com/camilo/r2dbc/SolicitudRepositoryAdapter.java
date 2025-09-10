package co.com.camilo.r2dbc;

import co.com.camilo.model.solicitud.Estado;
import co.com.camilo.model.solicitud.Prestamo;
import co.com.camilo.model.solicitud.Solicitud;
import co.com.camilo.model.solicitud.gateways.SolicitudRepository;
import co.com.camilo.r2dbc.entity.SolicitudEntity;
import co.com.camilo.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SolicitudRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Integer,
        SolicitudReactiveRepository
        > implements SolicitudRepository {

    public SolicitudRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, solicitud -> {
            if (solicitud == null) return null;
            return Solicitud.builder()
                    .id(solicitud.getId())
                    .monto(solicitud.getMonto())
                    .plazo(solicitud.getPlazo())
                    .email(solicitud.getEmail())
                    .estado(Estado.builder().id(solicitud.getEstado()).build())
                    .prestamo(Prestamo.builder().id(solicitud.getPrestamo()).build())
                    .build();
        });
    }

    @Override
    public Mono<Solicitud> save(Solicitud solicitud) {
        SolicitudEntity data = SolicitudEntity.builder()
                .id(solicitud.getId())
                .monto(solicitud.getMonto())
                .plazo(solicitud.getPlazo())
                .email(solicitud.getEmail())
                .estado(solicitud.getEstado() != null ? solicitud.getEstado().getId() : 0)
                .prestamo(solicitud.getPrestamo() != null ? solicitud.getPrestamo().getId() : 0)
                .build();

        return repository.save(data)
                .map(saved -> Solicitud.builder()
                        .id(saved.getId())
                        .monto(saved.getMonto())
                        .plazo(saved.getPlazo())
                        .email(saved.getEmail())
                        .estado(Estado.builder().id(saved.getEstado()).build())
                        .prestamo(Prestamo.builder().id(saved.getPrestamo()).build())
                        .build()
                );
    }

    @Override
    public Mono<Solicitud> findById(int id) {
        return repository.findById(id)
                .map(d -> Solicitud.builder()
                        .id(d.getId())
                        .monto(d.getMonto())
                        .plazo(d.getPlazo())
                        .email(d.getEmail())
                        .estado(Estado.builder().id(d.getEstado()).build())
                        .prestamo(Prestamo.builder().id(d.getPrestamo()).build())
                        .build()
                );
    }

    @Override
    public Flux<Solicitud> findSolicitudesPendientesConFiltros(Integer plazo, String email, String nombre, Integer tipoPrestamo, Integer estadoSolicitud, int pagina, int tamano) {
        int offset = pagina * tamano;
        return repository.findSolicitudesPendientesConFiltros(plazo, email, nombre, tipoPrestamo, estadoSolicitud, tamano, offset)
                .map(this::mapToSolicitudWithDetails);
    }

    @Override
    public Mono<Long> countSolicitudesPendientesConFiltros(Integer plazo, String email, String nombre, Integer tipoPrestamo, Integer estadoSolicitud) {
        return repository.countSolicitudesPendientesConFiltros(plazo, email, nombre, tipoPrestamo, estadoSolicitud);
    }

    @Override
    public Mono<Long> sumMontoSolicitudesAprobadas() {
        return repository.sumMontoSolicitudesAprobadas();
    }

    private Solicitud mapToSolicitudWithDetails(SolicitudEntity entity) {
        return Solicitud.builder()
                .id(entity.getId())
                .monto(entity.getMonto())
                .plazo(entity.getPlazo())
                .email(entity.getEmail())
                .estado(Estado.builder().id(entity.getEstado()).build())
                .prestamo(Prestamo.builder()
                        .id(entity.getPrestamo())
                        .nombre(entity.getNombrePrestamo())
                        .tasaInteres(entity.getTasaInteres())
                        .build())
                .nombre(entity.getNombreUsuario())
                .salarioBase(entity.getSalarioBase())
                .build();
    }
}

