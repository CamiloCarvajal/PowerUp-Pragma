package co.com.camilo.model.rol;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("Rol Model Tests")
class RolTest {

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = Rol.builder()
                .id(1)
                .nombre("Administrador")
                .descripcion("Rol con permisos administrativos")
                .build();
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {
        
        @Test
        @DisplayName("Should build rol with all fields")
        void shouldBuildRolWithAllFields() {
            assertNotNull(rol);
            assertEquals(1, rol.getId());
            assertEquals("Administrador", rol.getNombre());
            assertEquals("Rol con permisos administrativos", rol.getDescripcion());
        }

        @Test
        @DisplayName("Should build rol with minimal fields")
        void shouldBuildRolWithMinimalFields() {
            Rol minimalRol = Rol.builder()
                    .id(2)
                    .nombre("Usuario")
                    .build();

            assertNotNull(minimalRol);
            assertEquals(2, minimalRol.getId());
            assertEquals("Usuario", minimalRol.getNombre());
            assertNull(minimalRol.getDescripcion());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {
        
        @Test
        @DisplayName("Should get and set all fields")
        void shouldGetAndSetAllFields() {
            // Test setters
            rol.setId(3);
            rol.setNombre("Editor");
            rol.setDescripcion("Rol con permisos de edición");

            // Test getters
            assertEquals(3, rol.getId());
            assertEquals("Editor", rol.getNombre());
            assertEquals("Rol con permisos de edición", rol.getDescripcion());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {
        
        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(rol, rol);
        }

        @Test
        @DisplayName("Should be equal to rol with same id")
        void shouldBeEqualToRolWithSameId() {
            Rol sameRol = Rol.builder()
                    .id(1)
                    .nombre("Different")
                    .descripcion("Different description")
                    .build();

            assertEquals(rol, sameRol);
            assertEquals(rol.hashCode(), sameRol.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to rol with different id")
        void shouldNotBeEqualToRolWithDifferentId() {
            Rol differentRol = Rol.builder()
                    .id(2)
                    .nombre("Administrador")
                    .descripcion("Rol con permisos administrativos")
                    .build();

            assertNotEquals(rol, differentRol);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, rol);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertNotEquals("String", rol);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            Rol rolWithNulls = Rol.builder()
                    .id(0)
                    .nombre(null)
                    .descripcion(null)
                    .build();

            assertEquals(0, rolWithNulls.getId());
            assertNull(rolWithNulls.getNombre());
            assertNull(rolWithNulls.getDescripcion());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            rol.setNombre("");
            rol.setDescripcion("");

            assertEquals("", rol.getNombre());
            assertEquals("", rol.getDescripcion());
        }

        @Test
        @DisplayName("Should handle zero id")
        void shouldHandleZeroId() {
            rol.setId(0);
            assertEquals(0, rol.getId());
        }

        @Test
        @DisplayName("Should handle negative id")
        void shouldHandleNegativeId() {
            rol.setId(-1);
            assertEquals(-1, rol.getId());
        }
    }

//    @Nested
//    @DisplayName("ToString Tests")
//    class ToStringTests {
//
//        @Test
//        @DisplayName("Should generate toString with all fields")
//        void shouldGenerateToStringWithAllFields() {
//            String toString = rol.toString();
//
//            assertNotNull(toString);
//            assertTrue(toString.contains("Rol"));
//            assertTrue(toString.contains("1"));
//            assertTrue(toString.contains("Administrador"));
//            assertTrue(toString.contains("Rol con permisos administrativos"));
//        }
//
//        @Test
//        @DisplayName("Should generate toString with null fields")
//        void shouldGenerateToStringWithNullFields() {
//            Rol rolWithNulls = Rol.builder()
//                    .id(1)
//                    .nombre(null)
//                    .descripcion(null)
//                    .build();
//
//            String toString = rolWithNulls.toString();
//
//            assertNotNull(toString);
//            assertTrue(toString.contains("Rol"));
//            assertTrue(toString.contains("1"));
//        }
//    }
}

