package co.com.camilo.model.autenticacion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

    private String token;
    private String tipoToken;
    private long tiempoExpiracion;
    private UsuarioAutenticado usuario;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsuarioAutenticado {
        private int id;
        private String nombre;
        private String apellido;
        private String correoElectronico;
        private int idRol;
        private String nombreRol;
    }

}

