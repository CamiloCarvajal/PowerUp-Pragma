package co.com.camilo.api;

import co.com.camilo.api.DTO.CreateSolicitudDto;
import co.com.camilo.model.solicitud.Estado;
import co.com.camilo.model.solicitud.Prestamo;
import co.com.camilo.model.solicitud.Solicitud;
import co.com.camilo.usecase.solicitud.SolicitudUseCase;

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
//                .flatMap(solicitudValidator::validarDatosEntrada)
                .flatMap(this::mapToSolicitud)
                .doOnNext(solicitud -> log.debug("Solicitud Mapped {}", solicitud))
                .flatMap(solicitudUseCase::crearSolicitud)
                .flatMap(this::construirRespuestaExitosa)
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
        } else {
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("details", "Ha ocurrido un error inesperado. Por favor, inténtelo más tarde. " + error.getMessage());
            return ServerResponse.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
        }
    }

    // Métodos existentes para mantener compatibilidad
    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("Endpoint GET funcionando");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("Otro endpoint GET funcionando");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("Endpoint POST funcionando");
    }
}
