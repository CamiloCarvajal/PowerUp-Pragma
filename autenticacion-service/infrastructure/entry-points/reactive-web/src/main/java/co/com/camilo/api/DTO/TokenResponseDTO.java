package co.com.camilo.api.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDTO {

    private String token;
    private String tipoToken;
    private long tiempoExpiracion;
    private UsuarioAutenticadoDTO usuario;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsuarioAutenticadoDTO {
        private int id;
        private String nombre;
        private String apellido;
        private String correoElectronico;
        private int idRol;
        private String nombreRol;
    }

}

