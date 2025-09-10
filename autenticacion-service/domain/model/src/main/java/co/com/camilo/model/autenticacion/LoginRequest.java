package co.com.camilo.model.autenticacion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    private String correoElectronico;

    private String password;

}

