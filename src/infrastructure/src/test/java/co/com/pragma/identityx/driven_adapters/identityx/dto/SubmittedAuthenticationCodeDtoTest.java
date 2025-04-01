package co.com.pragma.identityx.driven_adapters.identityx.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubmittedAuthenticationCodeDtoTest {

    @Test
    void testBuilder() {
        // Arrange
        String expectedCode = "123456";

        // Act
        SubmittedAuthenticationCodeDto dto = SubmittedAuthenticationCodeDto.builder()
                .submittedAuthenticationCode(expectedCode)
                .build();

        // Assert
        assertEquals(expectedCode, dto.getSubmittedAuthenticationCode());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        String expectedCode = "abcdef";

        // Act
        SubmittedAuthenticationCodeDto dto = new SubmittedAuthenticationCodeDto(expectedCode);

        // Assert
        assertEquals(expectedCode, dto.getSubmittedAuthenticationCode());
    }

    @Test
    void testSetterAndGetter() {
        // Arrange
        String expectedCode = "xyz123";
        SubmittedAuthenticationCodeDto dto = new SubmittedAuthenticationCodeDto(expectedCode);

        // Act
        dto.setSubmittedAuthenticationCode(expectedCode);

        // Assert
        assertEquals(expectedCode, dto.getSubmittedAuthenticationCode());
    }

    @Test
    void testToString() {
        // Arrange
        String expectedCode = "987654";
        SubmittedAuthenticationCodeDto dto = SubmittedAuthenticationCodeDto.builder()
                .submittedAuthenticationCode(expectedCode)
                .build();

        // Act
        String dtoString = dto.toString();

        // Assert
        assertTrue(dtoString.contains(expectedCode));
    }
}