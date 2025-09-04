package co.com.camilo.model.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("User Model Tests")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .nombre("Juan")
                .apellido("Pérez")
                .correoElectronico("juan@email.com")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123 #45-67")
                .telefono("+57 300 123 4567")
                .salarioBase(50000)
                .build();
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {
        
        @Test
        @DisplayName("Should build user with all fields")
        void shouldBuildUserWithAllFields() {
            assertNotNull(user);
            assertEquals(1, user.getId());
            assertEquals("Juan", user.getNombre());
            assertEquals("Pérez", user.getApellido());
            assertEquals("juan@email.com", user.getCorreoElectronico());
            assertEquals(LocalDate.of(1990, 1, 1), user.getFechaNacimiento());
            assertEquals("Calle 123 #45-67", user.getDireccion());
            assertEquals("+57 300 123 4567", user.getTelefono());
            assertEquals(50000, user.getSalarioBase());
        }

        @Test
        @DisplayName("Should build user with minimal fields")
        void shouldBuildUserWithMinimalFields() {
            User minimalUser = User.builder()
                    .nombre("Ana")
                    .correoElectronico("ana@email.com")
                    .build();

            assertNotNull(minimalUser);
            assertEquals("Ana", minimalUser.getNombre());
            assertEquals("ana@email.com", minimalUser.getCorreoElectronico());
            assertEquals(0, minimalUser.getId());
            assertEquals(0, minimalUser.getSalarioBase());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {
        
        @Test
        @DisplayName("Should get and set all fields")
        void shouldGetAndSetAllFields() {
            // Test setters
            user.setId(2);
            user.setNombre("María");
            user.setApellido("García");
            user.setCorreoElectronico("maria@email.com");
            user.setFechaNacimiento(LocalDate.of(1995, 5, 15));
            user.setDireccion("Avenida 456 #78-90");
            user.setTelefono("+57 310 987 6543");
            user.setSalarioBase(75000);

            // Test getters
            assertEquals(2, user.getId());
            assertEquals("María", user.getNombre());
            assertEquals("García", user.getApellido());
            assertEquals("maria@email.com", user.getCorreoElectronico());
            assertEquals(LocalDate.of(1995, 5, 15), user.getFechaNacimiento());
            assertEquals("Avenida 456 #78-90", user.getDireccion());
            assertEquals("+57 310 987 6543", user.getTelefono());
            assertEquals(75000, user.getSalarioBase());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {
        
        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(user, user);
        }

        @Test
        @DisplayName("Should be equal to user with same id")
        void shouldBeEqualToUserWithSameId() {
            User sameUser = User.builder()
                    .id(1)
                    .nombre("Different")
                    .correoElectronico("different@email.com")
                    .build();

            assertEquals(user, sameUser);
        }

        @Test
        @DisplayName("Should not be equal to user with different id")
        void shouldNotBeEqualToUserWithDifferentId() {
            User differentUser = User.builder()
                    .id(2)
                    .nombre("Juan")
                    .correoElectronico("juan@email.com")
                    .build();

            assertNotEquals(user, differentUser);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, user);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertNotEquals("String", user);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            User userWithNulls = User.builder()
                    .nombre(null)
                    .apellido(null)
                    .correoElectronico(null)
                    .direccion(null)
                    .telefono(null)
                    .build();

            assertNull(userWithNulls.getNombre());
            assertNull(userWithNulls.getApellido());
            assertNull(userWithNulls.getCorreoElectronico());
            assertNull(userWithNulls.getDireccion());
            assertNull(userWithNulls.getTelefono());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            user.setNombre("");
            user.setApellido("");
            user.setCorreoElectronico("");

            assertEquals("", user.getNombre());
            assertEquals("", user.getApellido());
            assertEquals("", user.getCorreoElectronico());
        }

        @Test
        @DisplayName("Should handle zero values")
        void shouldHandleZeroValues() {
            user.setId(0);
            user.setSalarioBase(0);

            assertEquals(0, user.getId());
            assertEquals(0, user.getSalarioBase());
        }
    }
}