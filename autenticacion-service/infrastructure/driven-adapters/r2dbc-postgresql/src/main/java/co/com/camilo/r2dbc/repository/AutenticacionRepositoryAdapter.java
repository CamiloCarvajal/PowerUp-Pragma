package co.com.camilo.r2dbc.repository;

import co.com.camilo.model.autenticacion.UsuarioAutenticado;
import co.com.camilo.model.autenticacion.gateways.AutenticacionRepository;
import co.com.camilo.model.autenticacion.gateways.PasswordEncoder;
import co.com.camilo.model.exceptions.AutenticacionException;
import co.com.camilo.model.exceptions.CredencialesInvalidasException;
import co.com.camilo.model.exceptions.UsuarioNoEncontradoException;
import co.com.camilo.model.rol.Rol;
import co.com.camilo.model.user.User;
import co.com.camilo.model.user.gateways.UserRepository;
import co.com.camilo.model.rol.gateways.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AutenticacionRepositoryAdapter implements AutenticacionRepository {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UsuarioAutenticado> autenticarUsuario(String correoElectronico, String password) {
        return userRepository.findByEmail(correoElectronico)
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException("Usuario no encontrado con el correo: " + correoElectronico)))
                .flatMap(usuario -> {
                    if (!passwordEncoder.matches(password, usuario.getPassword())) {
                        return Mono.error(new CredencialesInvalidasException("Credenciales inválidas"));
                    }
                    return rolRepository.findById(usuario.getIdRol())
                            .switchIfEmpty(Mono.error(new RuntimeException("Rol no encontrado para el usuario")))
                            .map(rol -> mapearAUsuarioAutenticado(usuario, rol));
                })
                .doOnNext(usuario -> log.debug("Usuario autenticado: {}", usuario.getCorreoElectronico()))
                .onErrorMap(e -> e instanceof UsuarioNoEncontradoException || e instanceof CredencialesInvalidasException ? 
                    e : new AutenticacionException("Error durante la autenticación: " + e.getMessage(), e));
    }

    private UsuarioAutenticado mapearAUsuarioAutenticado(User usuario, Rol rol) {
        return UsuarioAutenticado.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correoElectronico(usuario.getCorreoElectronico())
                .idRol(usuario.getIdRol())
                .nombreRol(rol.getNombre())
                .build();
    }
}
