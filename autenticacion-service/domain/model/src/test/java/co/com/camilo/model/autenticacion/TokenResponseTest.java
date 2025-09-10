package co.com.camilo.model.autenticacion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("TokenResponse Model Tests")
class TokenResponseTest {

    private TokenResponse tokenResponse;
    private TokenResponse.UsuarioAutenticado usuarioAutenticado;

    @BeforeEach
    void setUp() {
        usuarioAutenticado = TokenResponse.UsuarioAutenticado.builder()
                .id(1)
                .nombre("Juan")
                .apellido("Pérez")
                .correoElectronico("juan@email.com")
                .idRol(1)
                .nombreRol("Administrador")
                .build();

        tokenResponse = TokenResponse.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .tipoToken("Bearer")
                .tiempoExpiracion(86400000L)
                .usuario(usuarioAutenticado)
                .build();
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {
        
        @Test
        @DisplayName("Should build token response with all fields")
        void shouldBuildTokenResponseWithAllFields() {
            assertNotNull(tokenResponse);
            assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", tokenResponse.getToken());
            assertEquals("Bearer", tokenResponse.getTipoToken());
            assertEquals(86400000L, tokenResponse.getTiempoExpiracion());
            assertNotNull(tokenResponse.getUsuario());
            assertEquals(usuarioAutenticado, tokenResponse.getUsuario());
        }

        @Test
        @DisplayName("Should build token response with minimal fields")
        void shouldBuildTokenResponseWithMinimalFields() {
            TokenResponse minimalResponse = TokenResponse.builder()
                    .token("minimal-token")
                    .build();

            assertNotNull(minimalResponse);
            assertEquals("minimal-token", minimalResponse.getToken());
            assertNull(minimalResponse.getTipoToken());
            assertEquals(0L, minimalResponse.getTiempoExpiracion());
            assertNull(minimalResponse.getUsuario());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {
        
        @Test
        @DisplayName("Should get and set all fields")
        void shouldGetAndSetAllFields() {
            TokenResponse.UsuarioAutenticado newUsuario = TokenResponse.UsuarioAutenticado.builder()
                    .id(2)
                    .nombre("María")
                    .apellido("García")
                    .correoElectronico("maria@email.com")
                    .idRol(2)
                    .nombreRol("Usuario")
                    .build();

            // Test setters
            tokenResponse.setToken("new-token");
            tokenResponse.setTipoToken("JWT");
            tokenResponse.setTiempoExpiracion(3600000L);
            tokenResponse.setUsuario(newUsuario);

            // Test getters
            assertEquals("new-token", tokenResponse.getToken());
            assertEquals("JWT", tokenResponse.getTipoToken());
            assertEquals(3600000L, tokenResponse.getTiempoExpiracion());
            assertEquals(newUsuario, tokenResponse.getUsuario());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            TokenResponse responseWithNulls = TokenResponse.builder()
                    .token(null)
                    .tipoToken(null)
                    .tiempoExpiracion(0L)
                    .usuario(null)
                    .build();

            assertNull(responseWithNulls.getToken());
            assertNull(responseWithNulls.getTipoToken());
            assertEquals(0L, responseWithNulls.getTiempoExpiracion());
            assertNull(responseWithNulls.getUsuario());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            tokenResponse.setToken("");
            tokenResponse.setTipoToken("");

            assertEquals("", tokenResponse.getToken());
            assertEquals("", tokenResponse.getTipoToken());
        }

        @Test
        @DisplayName("Should handle zero expiration time")
        void shouldHandleZeroExpirationTime() {
            tokenResponse.setTiempoExpiracion(0L);
            assertEquals(0L, tokenResponse.getTiempoExpiracion());
        }

        @Test
        @DisplayName("Should handle negative expiration time")
        void shouldHandleNegativeExpirationTime() {
            tokenResponse.setTiempoExpiracion(-1000L);
            assertEquals(-1000L, tokenResponse.getTiempoExpiracion());
        }

        @Test
        @DisplayName("Should handle very large expiration time")
        void shouldHandleVeryLargeExpirationTime() {
            long largeTime = Long.MAX_VALUE;
            tokenResponse.setTiempoExpiracion(largeTime);
            assertEquals(largeTime, tokenResponse.getTiempoExpiracion());
        }
    }

    @Nested
    @DisplayName("UsuarioAutenticado Inner Class Tests")
    class UsuarioAutenticadoTests {
        
        @Test
        @DisplayName("Should build usuario autenticado with all fields")
        void shouldBuildUsuarioAutenticadoWithAllFields() {
            assertNotNull(usuarioAutenticado);
            assertEquals(1, usuarioAutenticado.getId());
            assertEquals("Juan", usuarioAutenticado.getNombre());
            assertEquals("Pérez", usuarioAutenticado.getApellido());
            assertEquals("juan@email.com", usuarioAutenticado.getCorreoElectronico());
            assertEquals(1, usuarioAutenticado.getIdRol());
            assertEquals("Administrador", usuarioAutenticado.getNombreRol());
        }

        @Test
        @DisplayName("Should get and set usuario autenticado fields")
        void shouldGetAndSetUsuarioAutenticadoFields() {
            // Test setters
            usuarioAutenticado.setId(3);
            usuarioAutenticado.setNombre("Carlos");
            usuarioAutenticado.setApellido("López");
            usuarioAutenticado.setCorreoElectronico("carlos@email.com");
            usuarioAutenticado.setIdRol(3);
            usuarioAutenticado.setNombreRol("Editor");

            // Test getters
            assertEquals(3, usuarioAutenticado.getId());
            assertEquals("Carlos", usuarioAutenticado.getNombre());
            assertEquals("López", usuarioAutenticado.getApellido());
            assertEquals("carlos@email.com", usuarioAutenticado.getCorreoElectronico());
            assertEquals(3, usuarioAutenticado.getIdRol());
            assertEquals("Editor", usuarioAutenticado.getNombreRol());
        }

        @Test
        @DisplayName("Should handle null values in usuario autenticado")
        void shouldHandleNullValuesInUsuarioAutenticado() {
            TokenResponse.UsuarioAutenticado usuarioWithNulls = TokenResponse.UsuarioAutenticado.builder()
                    .id(0)
                    .nombre(null)
                    .apellido(null)
                    .correoElectronico(null)
                    .idRol(0)
                    .nombreRol(null)
                    .build();

            assertEquals(0, usuarioWithNulls.getId());
            assertNull(usuarioWithNulls.getNombre());
            assertNull(usuarioWithNulls.getApellido());
            assertNull(usuarioWithNulls.getCorreoElectronico());
            assertEquals(0, usuarioWithNulls.getIdRol());
            assertNull(usuarioWithNulls.getNombreRol());
        }

        @Test
        @DisplayName("Should handle empty strings in usuario autenticado")
        void shouldHandleEmptyStringsInUsuarioAutenticado() {
            usuarioAutenticado.setNombre("");
            usuarioAutenticado.setApellido("");
            usuarioAutenticado.setCorreoElectronico("");
            usuarioAutenticado.setNombreRol("");

            assertEquals("", usuarioAutenticado.getNombre());
            assertEquals("", usuarioAutenticado.getApellido());
            assertEquals("", usuarioAutenticado.getCorreoElectronico());
            assertEquals("", usuarioAutenticado.getNombreRol());
        }
    }

//    @Nested
//    @DisplayName("ToString Tests")
//    class ToStringTests {
//
//        @Test
//        @DisplayName("Should generate toString with all fields")
//        void shouldGenerateToStringWithAllFields() {
//            String toString = tokenResponse.toString();
//
//            assertNotNull(toString);
//            assertTrue(toString.contains("TokenResponse"));
//            assertTrue(toString.contains("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."));
//            assertTrue(toString.contains("Bearer"));
//            assertTrue(toString.contains("86400000"));
//        }
//
//        @Test
//        @DisplayName("Should generate toString for usuario autenticado")
//        void shouldGenerateToStringForUsuarioAutenticado() {
//            String toString = usuarioAutenticado.toString();
//
//            assertNotNull(toString);
//            assertTrue(toString.contains("UsuarioAutenticado"));
//            assertTrue(toString.contains("1"));
//            assertTrue(toString.contains("Juan"));
//            assertTrue(toString.contains("Pérez"));
//            assertTrue(toString.contains("juan@email.com"));
//            assertTrue(toString.contains("Administrador"));
//        }
//    }

    @Nested
    @DisplayName("Token Format Tests")
    class TokenFormatTests {
        
        @Test
        @DisplayName("Should handle JWT token format")
        void shouldHandleJwtTokenFormat() {
            String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
            tokenResponse.setToken(jwtToken);

            assertEquals(jwtToken, tokenResponse.getToken());
        }

        @Test
        @DisplayName("Should handle custom token format")
        void shouldHandleCustomTokenFormat() {
            String customToken = "custom-token-12345";
            tokenResponse.setToken(customToken);

            assertEquals(customToken, tokenResponse.getToken());
        }

        @Test
        @DisplayName("Should handle different token types")
        void shouldHandleDifferentTokenTypes() {
            String[] tokenTypes = {"Bearer", "JWT", "OAuth", "Custom"};
            
            for (String tokenType : tokenTypes) {
                tokenResponse.setTipoToken(tokenType);
                assertEquals(tokenType, tokenResponse.getTipoToken());
            }
        }
    }
}

