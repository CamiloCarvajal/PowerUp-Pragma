package co.com.camilo.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Autenticación")
                        .description("Servicio de autenticación y gestión de usuarios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Camilo")
                                .email("camilocc597@gmail.com")
                                .url("https://github.com/in/camilocarvajal"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo")
//                        new Server()
//                                .url("https://api.produccion.com")
//                                .description("Servidor de producción")
                ));
    }
}
