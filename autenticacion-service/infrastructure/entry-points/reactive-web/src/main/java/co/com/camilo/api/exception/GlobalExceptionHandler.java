package co.com.camilo.api.exception;

import java.time.LocalDateTime;
import java.util.Map;

import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;


@Component
public class GlobalExceptionHandler {

    public Mono<ServerResponse> handleAnyException(Throwable ex, ServerRequest request) {
        HttpStatus status = determineHttpStatus(ex);

        Map<String, Object> errorResponse = Map.of(
            "status", status.value(),
            "error", status.getReasonPhrase(),
            "message", ex.getMessage()
            // "timestamp", LocalDateTime.now(),
            // "path", request.path(),
            // "errorCode", ex.getClass().getSimpleName()
        );

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    private HttpStatus determineHttpStatus(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;        // 400
        }

        if (ex instanceof IllegalStateException) {
            return HttpStatus.CONFLICT;           // 409
        }

        if (ex instanceof NullPointerException) {
            return HttpStatus.BAD_REQUEST;        // 400
        }

        if (ex instanceof UnsupportedOperationException) {
            return HttpStatus.METHOD_NOT_ALLOWED; // 405
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;  // 500
    }
}
