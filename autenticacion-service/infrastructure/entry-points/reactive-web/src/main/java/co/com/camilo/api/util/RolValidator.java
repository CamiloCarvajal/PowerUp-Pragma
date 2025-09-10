package co.com.camilo.api.util;

import co.com.camilo.model.exceptions.AccesoDenegadoException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collection;

public class RolValidator {

    public static Mono<Void> validarRolAdminOAsesor(Authentication authentication) {
        return Mono.fromCallable(() -> {

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AccesoDenegadoException("Usuario no autenticado");
            }

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean tienePermiso = authorities.stream()
                    .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()) || 
                                     "ROLE_ASESOR".equals(auth.getAuthority()));

            if (!tienePermiso) {
                throw new AccesoDenegadoException("Acceso denegado. Se requiere rol de ADMIN o ASESOR");
            }

            return null;
        }).then();
    }

    public static Mono<Void> validarRolCliente(Authentication authentication) {
        return Mono.fromCallable(() -> {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AccesoDenegadoException("Usuario no autenticado");
            }

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean tienePermiso = authorities.stream()
                    .anyMatch(auth -> "ROLE_CLIENTE".equals(auth.getAuthority()));

            if (!tienePermiso) {
                throw new AccesoDenegadoException("Acceso denegado. Se requiere rol de CLIENTE");
            }

            return null;
        }).then();
    }

    public static Mono<Integer> obtenerIdUsuario(Authentication authentication) {
        return Mono.fromCallable(() -> {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AccesoDenegadoException("Usuario no autenticado");
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof co.com.camilo.model.autenticacion.UsuarioAutenticado) {
                return ((co.com.camilo.model.autenticacion.UsuarioAutenticado) principal).getId();
            }

            throw new AccesoDenegadoException("No se pudo obtener la información del usuario");
        });
    }
}

