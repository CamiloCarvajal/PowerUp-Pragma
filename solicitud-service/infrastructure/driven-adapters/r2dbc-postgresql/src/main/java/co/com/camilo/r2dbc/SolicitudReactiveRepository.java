package co.com.camilo.r2dbc;

import co.com.camilo.r2dbc.entity.SolicitudEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Integer>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    @Query("""
        SELECT s.*, p.nombre as nombre_prestamo, p.tasa_interes, u.nombre as nombre_usuario, u.salario_base
        FROM solicitud s
        LEFT JOIN prestamo p ON s.id_tipo_prestamo = p.id_prestamo
        LEFT JOIN usuario u ON s.email = u.email
        WHERE s.id_estado IN (1, 2, 3)
        AND (:plazo IS NULL OR s.plazo = :plazo)
        AND (:email IS NULL OR s.email ILIKE CONCAT('%', :email, '%'))
        AND (:nombre IS NULL OR u.nombre ILIKE CONCAT('%', :nombre, '%'))
        AND (:tipoPrestamo IS NULL OR s.id_tipo_prestamo = :tipoPrestamo)
        AND (:estadoSolicitud IS NULL OR s.id_estado = :estadoSolicitud)
        ORDER BY s.id_solicitud DESC
        LIMIT :limit OFFSET :offset
        """)
    Flux<SolicitudEntity> findSolicitudesPendientesConFiltros(
            @Param("plazo") Integer plazo,
            @Param("email") String email,
            @Param("nombre") String nombre,
            @Param("tipoPrestamo") Integer tipoPrestamo,
            @Param("estadoSolicitud") Integer estadoSolicitud,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query("""
        SELECT COUNT(*)
        FROM solicitud s
        LEFT JOIN usuario u ON s.email = u.email
        WHERE s.id_estado IN (1, 2, 3)
        AND (:plazo IS NULL OR s.plazo = :plazo)
        AND (:email IS NULL OR s.email ILIKE CONCAT('%', :email, '%'))
        AND (:nombre IS NULL OR u.nombre ILIKE CONCAT('%', :nombre, '%'))
        AND (:tipoPrestamo IS NULL OR s.id_tipo_prestamo = :tipoPrestamo)
        AND (:estadoSolicitud IS NULL OR s.id_estado = :estadoSolicitud)
        """)
    Mono<Long> countSolicitudesPendientesConFiltros(
            @Param("plazo") Integer plazo,
            @Param("email") String email,
            @Param("nombre") String nombre,
            @Param("tipoPrestamo") Integer tipoPrestamo,
            @Param("estadoSolicitud") Integer estadoSolicitud
    );

    @Query("SELECT COALESCE(SUM(monto), 0) FROM solicitud WHERE id_estado = 4")
    Mono<Long> sumMontoSolicitudesAprobadas();
}
