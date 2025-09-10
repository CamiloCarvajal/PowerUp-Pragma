package co.com.camilo.model.autenticacion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DisplayName("LoginRequest Model Tests")
class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .correoElectronico("usuario@email.com")
                .password("password123")
                .build();
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {
        
        @Test
        @DisplayName("Should build login request with all fields")
        void shouldBuildLoginRequestWithAllFields() {
            assertNotNull(loginRequest);
            assertEquals("usuario@email.com", loginRequest.getCorreoElectronico());
            assertEquals("password123", loginRequest.getPassword());
        }

        @Test
        @DisplayName("Should build login request with minimal fields")
        void shouldBuildLoginRequestWithMinimalFields() {
            LoginRequest minimalRequest = LoginRequest.builder()
                    .correoElectronico("test@email.com")
                    .build();

            assertNotNull(minimalRequest);
            assertEquals("test@email.com", minimalRequest.getCorreoElectronico());
            assertNull(minimalRequest.getPassword());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {
        
        @Test
        @DisplayName("Should get and set all fields")
        void shouldGetAndSetAllFields() {
            // Test setters
            loginRequest.setCorreoElectronico("nuevo@email.com");
            loginRequest.setPassword("nuevapassword456");

            // Test getters
            assertEquals("nuevo@email.com", loginRequest.getCorreoElectronico());
            assertEquals("nuevapassword456", loginRequest.getPassword());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            LoginRequest requestWithNulls = LoginRequest.builder()
                    .correoElectronico(null)
                    .password(null)
                    .build();

            assertNull(requestWithNulls.getCorreoElectronico());
            assertNull(requestWithNulls.getPassword());
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            loginRequest.setCorreoElectronico("");
            loginRequest.setPassword("");

            assertEquals("", loginRequest.getCorreoElectronico());
            assertEquals("", loginRequest.getPassword());
        }

        @Test
        @DisplayName("Should handle blank strings")
        void shouldHandleBlankStrings() {
            loginRequest.setCorreoElectronico("   ");
            loginRequest.setPassword("   ");

            assertEquals("   ", loginRequest.getCorreoElectronico());
            assertEquals("   ", loginRequest.getPassword());
        }

        @Test
        @DisplayName("Should handle special characters in email")
        void shouldHandleSpecialCharactersInEmail() {
            String specialEmail = "usuario+test@domain.co.uk";
            loginRequest.setCorreoElectronico(specialEmail);

            assertEquals(specialEmail, loginRequest.getCorreoElectronico());
        }

        @Test
        @DisplayName("Should handle special characters in password")
        void shouldHandleSpecialCharactersInPassword() {
            String specialPassword = "P@ssw0rd!#$%^&*()";
            loginRequest.setPassword(specialPassword);

            assertEquals(specialPassword, loginRequest.getPassword());
        }

        @Test
        @DisplayName("Should handle very long strings")
        void shouldHandleVeryLongStrings() {
            String longEmail = "a".repeat(100) + "@email.com";
            String longPassword = "p".repeat(1000);

            loginRequest.setCorreoElectronico(longEmail);
            loginRequest.setPassword(longPassword);

            assertEquals(longEmail, loginRequest.getCorreoElectronico());
            assertEquals(longPassword, loginRequest.getPassword());
        }
    }

//    @Nested
//    @DisplayName("ToString Tests")
//    class ToStringTests {
//
//        @Test
//        @DisplayName("Should generate toString with all fields")
//        void shouldGenerateToStringWithAllFields() {
//            String toString = loginRequest.toString();
//
//            assertNotNull(toString);
//            assertTrue(toString.contains("LoginRequest"));
//            assertTrue(toString.contains("usuario@email.com"));
//            assertTrue(toString.contains("password123"));
//        }
//
//        @Test
//        @DisplayName("Should generate toString with null fields")
//        void shouldGenerateToStringWithNullFields() {
//            LoginRequest requestWithNulls = LoginRequest.builder()
//                    .correoElectronico(null)
//                    .password(null)
//                    .build();
//
//            String toString = requestWithNulls.toString();
//
//            assertNotNull(toString);
//            assertTrue(toString.contains("LoginRequest"));
//        }
//    }

    @Nested
    @DisplayName("Validation Scenarios Tests")
    class ValidationScenariosTests {
        
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
                loginRequest.setCorreoElectronico(email);
                assertEquals(email, loginRequest.getCorreoElectronico());
            }
        }

        @Test
        @DisplayName("Should handle various password formats")
        void shouldHandleVariousPasswordFormats() {
            String[] passwords = {
                "simple",
                "password123",
                "P@ssw0rd!",
                "123456789",
                "abcdefghijklmnopqrstuvwxyz"
            };

            for (String password : passwords) {
                loginRequest.setPassword(password);
                assertEquals(password, loginRequest.getPassword());
            }
        }
    }
}

