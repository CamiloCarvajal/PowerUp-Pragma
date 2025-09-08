package co.com.camilo.model.autenticacion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("UsuarioAutenticado Model Tests")
class UsuarioAutenticadoTest {

    private UsuarioAutenticado usuarioAutenticado;

    @BeforeEach
    void setUp() {
        usuarioAutenticado = UsuarioAutenticado.builder()
                .id(1)
                .nombre("Juan")
                .apellido("Pérez")
                .correoElectronico("juan@email.com")
                .idRol(1)
                .nombreRol("Administrador")
                .build();
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {
        
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
        @DisplayName("Should build usuario autenticado with minimal fields")
        void shouldBuildUsuarioAutenticadoWithMinimalFields() {
            UsuarioAutenticado minimalUsuario = UsuarioAutenticado.builder()
                    .id(2)
                    .nombre("Ana")
                    .correoElectronico("ana@email.com")
                    .build();

            assertNotNull(minimalUsuario);
            assertEquals(2, minimalUsuario.getId());
            assertEquals("Ana", minimalUsuario.getNombre());
            assertEquals("ana@email.com", minimalUsuario.getCorreoElectronico());
            assertEquals(0, minimalUsuario.getIdRol());
            assertNull(minimalUsuario.getApellido());
            assertNull(minimalUsuario.getNombreRol());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {
        
        @Test
        @DisplayName("Should get and set all fields")
        void shouldGetAndSetAllFields() {
            // Test setters
            usuarioAutenticado.setId(3);
            usuarioAutenticado.setNombre("María");
            usuarioAutenticado.setApellido("García");
            usuarioAutenticado.setCorreoElectronico("maria@email.com");
            usuarioAutenticado.setIdRol(2);
            usuarioAutenticado.setNombreRol("Usuario");

            // Test getters
            assertEquals(3, usuarioAutenticado.getId());
            assertEquals("María", usuarioAutenticado.getNombre());
            assertEquals("García", usuarioAutenticado.getApellido());
            assertEquals("maria@email.com", usuarioAutenticado.getCorreoElectronico());
            assertEquals(2, usuarioAutenticado.getIdRol());
            assertEquals("Usuario", usuarioAutenticado.getNombreRol());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            UsuarioAutenticado usuarioWithNulls = UsuarioAutenticado.builder()
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
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            usuarioAutenticado.setNombre("");
            usuarioAutenticado.setApellido("");
            usuarioAutenticado.setCorreoElectronico("");
            usuarioAutenticado.setNombreRol("");

            assertEquals("", usuarioAutenticado.getNombre());
            assertEquals("", usuarioAutenticado.getApellido());
            assertEquals("", usuarioAutenticado.getCorreoElectronico());
            assertEquals("", usuarioAutenticado.getNombreRol());
        }

        @Test
        @DisplayName("Should handle blank strings")
        void shouldHandleBlankStrings() {
            usuarioAutenticado.setNombre("   ");
            usuarioAutenticado.setApellido("   ");
            usuarioAutenticado.setCorreoElectronico("   ");
            usuarioAutenticado.setNombreRol("   ");

            assertEquals("   ", usuarioAutenticado.getNombre());
            assertEquals("   ", usuarioAutenticado.getApellido());
            assertEquals("   ", usuarioAutenticado.getCorreoElectronico());
            assertEquals("   ", usuarioAutenticado.getNombreRol());
        }

        @Test
        @DisplayName("Should handle zero id")
        void shouldHandleZeroId() {
            usuarioAutenticado.setId(0);
            assertEquals(0, usuarioAutenticado.getId());
        }

        @Test
        @DisplayName("Should handle negative id")
        void shouldHandleNegativeId() {
            usuarioAutenticado.setId(-1);
            assertEquals(-1, usuarioAutenticado.getId());
        }

        @Test
        @DisplayName("Should handle zero rol id")
        void shouldHandleZeroRolId() {
            usuarioAutenticado.setIdRol(0);
            assertEquals(0, usuarioAutenticado.getIdRol());
        }

        @Test
        @DisplayName("Should handle negative rol id")
        void shouldHandleNegativeRolId() {
            usuarioAutenticado.setIdRol(-1);
            assertEquals(-1, usuarioAutenticado.getIdRol());
        }
    }

//    @Nested
//    @DisplayName("ToString Tests")
//    class ToStringTests {
//
//        @Test
//        @DisplayName("Should generate toString with all fields")
//        void shouldGenerateToStringWithAllFields() {
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
//
//        @Test
//        @DisplayName("Should generate toString with null fields")
//        void shouldGenerateToStringWithNullFields() {
//            UsuarioAutenticado usuarioWithNulls = UsuarioAutenticado.builder()
//                    .id(1)
//                    .nombre(null)
//                    .apellido(null)
//                    .correoElectronico(null)
//                    .idRol(0)
//                    .nombreRol(null)
//                    .build();
//
//            String toString = usuarioWithNulls.toString();
//
//            assertNotNull(toString);
//            assertTrue(toString.contains("UsuarioAutenticado"));
//            assertTrue(toString.contains("1"));
//        }
//    }

    @Nested
    @DisplayName("Email Format Tests")
    class EmailFormatTests {
        
        @Test
        @DisplayName("Should handle valid email formats")
        void shouldHandleValidEmailFormats() {
            String[] validEmails = {
                "user@domain.com",
                "user.name@domain.com",
                "user+tag@domain.co.uk",
                "user123@domain123.org",
                "a@b.c"
            };

            for (String email : validEmails) {
                usuarioAutenticado.setCorreoElectronico(email);
                assertEquals(email, usuarioAutenticado.getCorreoElectronico());
            }
        }

        @Test
        @DisplayName("Should handle special characters in email")
        void shouldHandleSpecialCharactersInEmail() {
            String specialEmail = "usuario+test@domain.co.uk";
            usuarioAutenticado.setCorreoElectronico(specialEmail);

            assertEquals(specialEmail, usuarioAutenticado.getCorreoElectronico());
        }
    }

    @Nested
    @DisplayName("Name Format Tests")
    class NameFormatTests {
        
        @Test
        @DisplayName("Should handle names with accents")
        void shouldHandleNamesWithAccents() {
            usuarioAutenticado.setNombre("José");
            usuarioAutenticado.setApellido("González");

            assertEquals("José", usuarioAutenticado.getNombre());
            assertEquals("González", usuarioAutenticado.getApellido());
        }

        @Test
        @DisplayName("Should handle names with spaces")
        void shouldHandleNamesWithSpaces() {
            usuarioAutenticado.setNombre("María José");
            usuarioAutenticado.setApellido("De la Cruz");

            assertEquals("María José", usuarioAutenticado.getNombre());
            assertEquals("De la Cruz", usuarioAutenticado.getApellido());
        }

        @Test
        @DisplayName("Should handle names with hyphens")
        void shouldHandleNamesWithHyphens() {
            usuarioAutenticado.setNombre("Ana-María");
            usuarioAutenticado.setApellido("García-López");

            assertEquals("Ana-María", usuarioAutenticado.getNombre());
            assertEquals("García-López", usuarioAutenticado.getApellido());
        }
    }

    @Nested
    @DisplayName("Role Tests")
    class RoleTests {
        
        @Test
        @DisplayName("Should handle different role names")
        void shouldHandleDifferentRoleNames() {
            String[] roleNames = {
                "Administrador",
                "Usuario",
                "Editor",
                "Moderador",
                "Super Usuario"
            };

            for (String roleName : roleNames) {
                usuarioAutenticado.setNombreRol(roleName);
                assertEquals(roleName, usuarioAutenticado.getNombreRol());
            }
        }

        @Test
        @DisplayName("Should handle role names with spaces")
        void shouldHandleRoleNamesWithSpaces() {
            usuarioAutenticado.setNombreRol("Super Administrador");
            assertEquals("Super Administrador", usuarioAutenticado.getNombreRol());
        }
    }
}

