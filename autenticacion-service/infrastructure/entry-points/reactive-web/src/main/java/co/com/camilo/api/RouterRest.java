package co.com.camilo.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser"
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    beanClass = AutenticacionHandler.class,
                    beanMethod = "iniciarSesion"
            ),
            @RouterOperation(
                    path = "/api/v1/solicitudes-prestamo",
                    beanClass = Handler.class,
                    beanMethod = "crearSolicitudPrestamo"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler, AutenticacionHandler autenticacionHandler) {
        return route(POST("/api/v1/usuarios"), handler::listenSaveUser)
                .andRoute(POST("/api/v1/login"), autenticacionHandler::iniciarSesion);
    }
}
