package co.com.camilo.api;

import co.com.camilo.api.DTO.LoginRequestDTO;
import co.com.camilo.api.DTO.TokenResponseDTO;
import co.com.camilo.api.exception.GlobalExceptionHandler;
import co.com.camilo.model.autenticacion.LoginRequest;
import co.com.camilo.model.autenticacion.TokenResponse;
import co.com.camilo.usecase.autenticacion.AutenticacionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class AutenticacionHandler {

    private final AutenticacionUseCase autenticacionUseCase;
    private final GlobalExceptionHandler exceptionHandler;

    @Operation(
            operationId = "iniciarSesion",
            summary = "Iniciar sesión",
            description = "Autentica un usuario y retorna un token JWT",
            tags = { "Autenticación" }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TokenResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))
    )
    public Mono<ServerResponse> iniciarSesion(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequestDTO.class)
                .doOnNext(request -> log.debug("Request de login recibido para: {}", request.getCorreoElectronico()))
                .map(this::mapearALoginRequest)
                .flatMap(autenticacionUseCase::autenticarUsuario)
                .map(this::mapearATokenResponseDTO)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .doOnNext(response -> log.info("Login exitoso"))
                .onErrorResume(Throwable.class, ex -> 
                    exceptionHandler.handleAnyException(ex, serverRequest));
    }

    private LoginRequest mapearALoginRequest(LoginRequestDTO dto) {
        return LoginRequest.builder()
                .correoElectronico(dto.getCorreoElectronico())
                .password(dto.getPassword())
                .build();
    }

    private TokenResponseDTO mapearATokenResponseDTO(TokenResponse response) {
        return TokenResponseDTO.builder()
                .token(response.getToken())
                .tipoToken(response.getTipoToken())
                .tiempoExpiracion(response.getTiempoExpiracion())
                .usuario(TokenResponseDTO.UsuarioAutenticadoDTO.builder()
                        .id(response.getUsuario().getId())
                        .nombre(response.getUsuario().getNombre())
                        .apellido(response.getUsuario().getApellido())
                        .correoElectronico(response.getUsuario().getCorreoElectronico())
                        .idRol(response.getUsuario().getIdRol())
                        .nombreRol(response.getUsuario().getNombreRol())
                        .build())
                .build();
    }
}

