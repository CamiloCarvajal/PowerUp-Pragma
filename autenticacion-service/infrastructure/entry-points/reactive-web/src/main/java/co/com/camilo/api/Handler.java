package co.com.camilo.api;

import co.com.camilo.api.DTO.CreateUserRequest;
import co.com.camilo.api.exception.GlobalExceptionHandler;
import co.com.camilo.model.user.User;
import co.com.camilo.usecase.user.UserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class Handler {

    private final UserUseCase userUseCase;
    private final Validator validator;
    private final GlobalExceptionHandler exceptionHandler;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }


    @Operation(
            operationId = "crearUsuario",
            summary = "Guardar usuario",
            description = "Crea un nuevo usuario en el sistema",
            tags = { "Usuarios" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserRequest.class)
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
            content = @Content(schema = @Schema(implementation = CreateUserRequest.class))
    )
    public Mono<ServerResponse> listenSaveUser(@Parameter(description = "Datos del usuario a crear") ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateUserRequest.class)
                .doOnNext(request -> log.debug("Request a procesar {}", request))
                .flatMap(this::validateCreateUserRequest)
                .doOnNext(request -> log.debug("Usuario validado {}", request))
                .flatMap(this::mapToUser)
                .doOnNext(request -> log.debug("Usuario mapeado {}", request))
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser))
                .onErrorResume(Throwable.class, ex -> 
                    exceptionHandler.handleAnyException(ex, serverRequest));
    }

    private Mono<CreateUserRequest> validateCreateUserRequest(CreateUserRequest request) {
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            throw new IllegalArgumentException("Errores de validación: " + String.join(", ", errors));
        }
        
        return Mono.just(request);
    }

    private Mono<User> mapToUser(CreateUserRequest request) {
        try {
            User user = User.builder()
                    .nombre(request.getNombre())
                    .apellido(request.getApellido())
                    .correoElectronico(request.getCorreoElectronico())
                    .fechaNacimiento(request.getFechaNacimiento())
                    .direccion(request.getDireccion())
                    .telefono(request.getTelefono())
                    .salarioBase(request.getSalarioBase())
                    .build();
            
            return Mono.just(user);
        } catch (Exception e) {
            throw new IllegalStateException("Error al mapear datos: " + e.getMessage());
        }
    }
}
