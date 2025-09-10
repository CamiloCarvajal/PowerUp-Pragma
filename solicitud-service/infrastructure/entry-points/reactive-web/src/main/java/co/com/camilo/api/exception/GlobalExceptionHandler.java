package co.com.camilo.api.exception;

import co.com.camilo.model.exceptions.AccesoDenegadoException;
import co.com.camilo.model.exceptions.AutenticacionException;
import co.com.camilo.model.exceptions.TokenInvalidoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		HttpStatus status = determineHttpStatus(ex);
		String errorCode = determineErrorCode(ex);
		String message = buildMessage(ex, errorCode);

		logError(status, errorCode, ex);

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		response.getHeaders().add("X-Error-Code", errorCode);

		Map<String, Object> body = new HashMap<>();
		body.put("success", false);
		body.put("errorCode", errorCode);
		body.put("message", message);

		byte[] bytes;
		try {
			bytes = objectMapper.writeValueAsBytes(body);
		} catch (Exception e) {
			String fallback = "{\"success\":false,\"errorCode\":\"INTERNAL_ERROR\",\"message\":\"[INTERNAL_ERROR] Error serializando respuesta\"}";
			bytes = fallback.getBytes(StandardCharsets.UTF_8);
		}

		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}

	private void logError(HttpStatus status, String errorCode, Throwable ex) {
		if (status.is5xxServerError()) {
			log.error("Handled exception [{}] -> {}", errorCode, ex.getMessage(), ex);
		} else {
			log.warn("Handled exception [{}] -> {}", errorCode, ex.getMessage());
		}
	}

	private HttpStatus determineHttpStatus(Throwable ex) {
		if (ex instanceof IllegalArgumentException) {
			return HttpStatus.BAD_REQUEST;
		}
		if (ex instanceof IllegalStateException) {
			return HttpStatus.CONFLICT;
		}
		if (ex instanceof AccesoDenegadoException) {
			return HttpStatus.FORBIDDEN;
		}
		if (ex instanceof TokenInvalidoException) {
			return HttpStatus.UNAUTHORIZED;
		}
		if (ex instanceof AutenticacionException) {
			return HttpStatus.UNAUTHORIZED;
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	private String determineErrorCode(Throwable ex) {
		if (ex instanceof IllegalArgumentException) {
			return "VALIDATION_ERROR";
		}
		if (ex instanceof IllegalStateException) {
			return "STATE_CONFLICT";
		}
		if (ex instanceof AccesoDenegadoException) {
			return "ACCESS_DENIED";
		}
		if (ex instanceof TokenInvalidoException) {
			return "TOKEN_INVALID";
		}
		if (ex instanceof AutenticacionException) {
			return "AUTHENTICATION_ERROR";
		}
		return "INTERNAL_ERROR";
	}

	private String buildMessage(Throwable ex, String code) {
		String base = ex.getMessage() != null ? ex.getMessage() : "Error inesperado";
		return "[" + code + "] " + base;
	}
}
