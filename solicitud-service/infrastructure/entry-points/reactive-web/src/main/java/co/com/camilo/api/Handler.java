package co.com.camilo.api;

import co.com.camilo.api.DTO.CreateSolicitudDto;
import co.com.camilo.api.DTO.SolicitudFiltrosDto;
import co.com.camilo.api.DTO.SolicitudesPaginadasResponseDto;
import co.com.camilo.api.DTO.SolicitudResponseDto;
import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import co.com.camilo.model.exceptions.AccesoDenegadoException;
import co.com.camilo.model.solicitud.Estado;
import co.com.camilo.model.solicitud.Prestamo;
import co.com.camilo.model.solicitud.Solicitud;
import co.com.camilo.model.solicitud.SolicitudConsultaDto;
import co.com.camilo.model.solicitud.SolicitudesPaginadasDto;
import co.com.camilo.usecase.solicitud.SolicitudUseCase;
import co.com.camilo.usecase.solicitud.ConsultarSolicitudesPendientesUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "API para gestión de usuarios")
public class Handler {

    private final SolicitudUseCase solicitudUseCase;
    private final ConsultarSolicitudesPendientesUseCase consultarSolicitudesPendientesUseCase;
    private final Validator validator;

    @Operation(
            operationId = "crearSolicitud",
            summary = "Guardar solicitud",
            description = "Crea un nuevo solicitud en el sistema",
            tags = { "Solicitudes" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitud creada exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Solicitud.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = CreateSolicitudDto.class))
    )
    public Mono<ServerResponse> crearSolicitud(ServerRequest serverRequest) {
        log.info("Recibida solicitud POST para crear solicitud de crédito");
        
        return serverRequest.bodyToMono(CreateSolicitudDto.class)
                .doOnNext(clase -> log.debug(" >> Clase {}", clase.prestamo()))
                .flatMap(this::validateCreateUserRequest)
                .flatMap(this::mapToSolicitud)
                .doOnNext(solicitud -> log.debug("Solicitud Mapped {}", solicitud))
                .flatMap(solicitud -> obtenerUsuarioAutenticado()
                .flatMap(usuario -> solicitudUseCase.crearSolicitud(solicitud, usuario)))
                .flatMap(this::construirRespuestaExitosa)
                .onErrorResume(this::manejarError);

    }

    @Operation(
            operationId = "consultarSolicitudesPendientes",
            summary = "Consultar solicitudes pendientes",
            description = "Consulta una lista paginada y filtrable de solicitudes pendientes (estados 1, 2, 3) para asesores",
            tags = { "Solicitudes" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitudes consultadas exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SolicitudesPaginadasResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de consulta inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuario no autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado - Solo asesores pueden consultar solicitudes"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public Mono<ServerResponse> consultarSolicitudesPendientes(ServerRequest serverRequest) {
        log.info("Recibida solicitud GET para consultar solicitudes pendientes");

        return extraerFiltrosDeRequest(serverRequest)
                .flatMap(filtros -> obtenerUsuarioAutenticado()
                        .flatMap(usuario -> consultarSolicitudesPendientesUseCase.consultarSolicitudesPendientes(
                                filtros.plazo(),
                                filtros.email(),
                                filtros.nombre(),
                                filtros.tipoPrestamo(),
                                filtros.estadoSolicitud(),
                                filtros.pagina() != null ? filtros.pagina() : 0,
                                filtros.tamano() != null ? filtros.tamano() : 10,
                                usuario
                        )))
                .flatMap(this::construirRespuestaConsultaExitosa)
                .onErrorResume(this::manejarError);
    }

    private Mono<CreateSolicitudDto> validateCreateUserRequest(CreateSolicitudDto request) {
        return Mono.defer(() -> {
            Set<ConstraintViolation<CreateSolicitudDto>> violations = validator.validate(request);

            if (!violations.isEmpty()) {
                List<String> errors = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());

                return Mono.error(new IllegalArgumentException(
                        "Errores de validación: " + String.join(", ", errors)
                ));
            }

            return Mono.just(request);
        });
    }

    private Mono<Solicitud> mapToSolicitud(CreateSolicitudDto request) {

        return Mono.fromCallable(() -> Solicitud.builder()
                .monto(request.monto())
                .plazo(request.plazo())
                .email(request.email())
                .estado(Estado.builder().id(1).build()) //Pendiente de revisión
                .prestamo(Prestamo.builder().id(request.prestamo()).build())
                .build()
        ).onErrorMap(e -> new IllegalStateException("Error al mapear datos: " + e.getMessage(), e));
    }

    private Mono<ServerResponse> construirRespuestaExitosa(Solicitud response) {
        log.info("Solicitud creada exitosamente con ID: {}", response.getId());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Solicitud creada exitosamente");
        responseBody.put("data", response);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

    private Mono<UsuarioAutenticado> obtenerUsuarioAutenticado() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (UsuarioAutenticado) securityContext.getAuthentication().getPrincipal())
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no autenticado")));
    }

    private Mono<ServerResponse> manejarError(Throwable error) {
        log.error("Error procesando solicitud: {}", error.getMessage(), error);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "Error al procesar la solicitud");
        
        if (error instanceof IllegalArgumentException) {
            errorResponse.put("error", "Datos inválidos");
            errorResponse.put("details", error.getMessage());
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
        } else if (error instanceof IllegalStateException) {
            errorResponse.put("error", "Error del sistema");
            errorResponse.put("details", error.getMessage());
            return ServerResponse.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
        } else if (error instanceof AccesoDenegadoException) {
            errorResponse.put("error", "Acceso denegado");
            errorResponse.put("details", error.getMessage());
            return ServerResponse.status(403)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
        } else if (error.getMessage() != null && error.getMessage().contains("autenticado")) {
            errorResponse.put("error", "Error de autenticación");
            errorResponse.put("details", error.getMessage());
            return ServerResponse.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
        } else {
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("details", "Ha ocurrido un error inesperado. Por favor, inténtelo más tarde. " + error.getMessage());
            return ServerResponse.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
        }
    }

    private Mono<SolicitudFiltrosDto> extraerFiltrosDeRequest(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> {
            String plazoStr = serverRequest.queryParam("plazo").orElse(null);
            String email = serverRequest.queryParam("email").orElse(null);
            String nombre = serverRequest.queryParam("nombre").orElse(null);
            String tipoPrestamoStr = serverRequest.queryParam("tipoPrestamo").orElse(null);
            String estadoSolicitudStr = serverRequest.queryParam("estadoSolicitud").orElse(null);
            String paginaStr = serverRequest.queryParam("pagina").orElse("0");
            String tamanoStr = serverRequest.queryParam("tamano").orElse("10");

            Integer plazo = null;
            if (plazoStr != null && !plazoStr.trim().isEmpty()) {
                try {
                    plazo = Integer.parseInt(plazoStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El parámetro 'plazo' debe ser un número entero válido");
                }
            }
            log.debug("--> Plazo {}",  plazo);
            Integer tipoPrestamo = null;
            if (tipoPrestamoStr != null && !tipoPrestamoStr.trim().isEmpty()) {
                try {
                    tipoPrestamo = Integer.parseInt(tipoPrestamoStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El parámetro 'tipoPrestamo' debe ser un número entero válido");
                }
            }

            Integer estadoSolicitud = null;
            if (estadoSolicitudStr != null && !estadoSolicitudStr.trim().isEmpty()) {
                try {
                    estadoSolicitud = Integer.parseInt(estadoSolicitudStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El parámetro 'estadoSolicitud' debe ser un número entero válido");
                }
            }

            int pagina;
            try {
                pagina = Integer.parseInt(paginaStr);
                if (pagina < 0) {
                    throw new IllegalArgumentException("El parámetro 'pagina' debe ser mayor o igual a 0");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El parámetro 'pagina' debe ser un número entero válido");
            }
            log.debug("--> Pagina {}",  pagina);

            int tamano;
            try {
                tamano = Integer.parseInt(tamanoStr);
                if (tamano <= 0 || tamano > 100) {
                    throw new IllegalArgumentException("El parámetro 'tamano' debe estar entre 1 y 100");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El parámetro 'tamano' debe ser un número entero válido");
            }

            return new SolicitudFiltrosDto(plazo, email, nombre, tipoPrestamo, estadoSolicitud, pagina, tamano);
        });
    }

    private Mono<ServerResponse> construirRespuestaConsultaExitosa(SolicitudesPaginadasDto response) {
        log.info("Solicitudes consultadas exitosamente. Total: {}, Página: {}/{}",
                response.getTotalElementos(), response.getPaginaActual() + 1, response.getTotalPaginas());

        SolicitudesPaginadasResponseDto responseDto = new SolicitudesPaginadasResponseDto(
                response.getSolicitudes().stream()
                        .map(this::mapToResponseDto)
                        .toList(),
                response.getTotalElementos(),
                response.getTotalPaginas(),
                response.getPaginaActual(),
                response.getTamanoPagina(),
                response.getDeudaTotalMensualSolicitudesAprobadas()
        );

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseDto);
    }

    private SolicitudResponseDto mapToResponseDto(SolicitudConsultaDto solicitud) {
        return new SolicitudResponseDto(
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getEmail(),
                solicitud.getNombre(),
                solicitud.getTipoPrestamo(),
                solicitud.getTasaInteres(),
                solicitud.getEstadoSolicitud(),
                solicitud.getSalarioBase()
        );
    }
}
