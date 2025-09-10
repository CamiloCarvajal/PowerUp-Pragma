package co.com.camilo.usecase.solicitud;

import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import co.com.camilo.model.exceptions.PrestamoNotFoundException;
import co.com.camilo.model.exceptions.AutenticacionException;
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
    
    public Mono<Solicitud> crearSolicitud(Solicitud solicitud, UsuarioAutenticado usuario) {
        return validarUsuarioCliente(usuario)
                .then(validarCorreoElectronico(solicitud, usuario))
                .then(validarTipoPrestamo(solicitud))
                .flatMap(estado -> crearSolicitudConEstado(solicitud))
                .flatMap(solicitudRepository::save);
    }
    
    private Mono<Solicitud> validarTipoPrestamo(Solicitud solicitud) {
        return tipoPrestamoRepository.findById(solicitud.getPrestamo().getId())
                .switchIfEmpty(Mono.error(new PrestamoNotFoundException(solicitud.getPrestamo().getId())))
                .thenReturn(solicitud);
    }
    
    private Mono<Solicitud> crearSolicitudConEstado(Solicitud request) {

        return tipoPrestamoRepository.findById(request.getPrestamo().getId())
                .map(tipoPrestamo -> Solicitud.builder()
                        .monto(request.getMonto())
                        .plazo(request.getPlazo())
                        .email(request.getEmail())
                        .estado(Estado.builder().id(ESTADO_PENDIENTE).build())
                        .prestamo(tipoPrestamo)
                        .build());
    }
    
    private Mono<Void> validarUsuarioCliente(UsuarioAutenticado usuario) {
        if (usuario == null) {
            return Mono.error(new AutenticacionException("Usuario no autenticado"));
        }
        
        if (!"CLIENTE".equalsIgnoreCase(usuario.getNombreRol())) {
            return Mono.error(new AutenticacionException("Solo los usuarios tipo CLIENTE pueden crear solicitudes de préstamo"));
        }
        
        return Mono.empty();
    }
    
    private Mono<Void> validarCorreoElectronico(Solicitud solicitud, UsuarioAutenticado usuario) {
        if (!usuario.getCorreoElectronico().equals(solicitud.getEmail())) {
            return Mono.error(new AutenticacionException("El correo electrónico de la solicitud debe coincidir con el del usuario autenticado"));
        }
        
        return Mono.empty();
    }

}
