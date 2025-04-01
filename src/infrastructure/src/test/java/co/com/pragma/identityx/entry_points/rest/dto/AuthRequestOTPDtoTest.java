package co.com.pragma.identityx.entry_points.rest.dto;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class AuthRequestOTPDtoTest {

    @Test
    void testBuilder() {
        // Arrange
        String fidoAuthenticationRequest = "fido_auth_request";
        String authenticationRequestId = "auth_request_id";
        Date expiration = new Date();
        Long availableRetries = 3L;
        Long totalRetriesAllowed = 5L;
        String authenticationCode = "123456";
        Date created = new Date();
        String status = "pending";
        String verificationResult = "success";

        // Act
        AuthRequestOTPDto dto = AuthRequestOTPDto.builder()
                .fidoAuthenticationRequest(fidoAuthenticationRequest)
                .authenticationRequestId(authenticationRequestId)
                .expiration(expiration)
                .availableRetries(availableRetries)
                .totalRetriesAllowed(totalRetriesAllowed)
                .authenticationCode(authenticationCode)
                .created(created)
                .status(status)
                .verificationResult(verificationResult)
                .build();

        // Assert
        assertEquals(fidoAuthenticationRequest, dto.getFidoAuthenticationRequest());
        assertEquals(authenticationRequestId, dto.getAuthenticationRequestId());
        assertEquals(expiration, dto.getExpiration());
        assertEquals(availableRetries, dto.getAvailableRetries());
        assertEquals(totalRetriesAllowed, dto.getTotalRetriesAllowed());
        assertEquals(authenticationCode, dto.getAuthenticationCode());
        assertEquals(created, dto.getCreated());
        assertEquals(status, dto.getStatus());
        assertEquals(verificationResult, dto.getVerificationResult());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        String fidoAuthenticationRequest = "fido_auth_request";
        String authenticationRequestId = "auth_request_id";
        Date expiration = new Date();
        Long availableRetries = 3L;
        Long totalRetriesAllowed = 5L;
        String authenticationCode = "123456";
        Date created = new Date();
        String status = "pending";
        String verificationResult = "success";

        // Act
        AuthRequestOTPDto dto = new AuthRequestOTPDto(
                fidoAuthenticationRequest,
                authenticationRequestId,
                expiration,
                availableRetries,
                totalRetriesAllowed,
                authenticationCode,
                created,
                status,
                verificationResult
        );

        // Assert
        assertEquals(fidoAuthenticationRequest, dto.getFidoAuthenticationRequest());
        assertEquals(authenticationRequestId, dto.getAuthenticationRequestId());
        assertEquals(expiration, dto.getExpiration());
        assertEquals(availableRetries, dto.getAvailableRetries());
        assertEquals(totalRetriesAllowed, dto.getTotalRetriesAllowed());
        assertEquals(authenticationCode, dto.getAuthenticationCode());
        assertEquals(created, dto.getCreated());
        assertEquals(status, dto.getStatus());
        assertEquals(verificationResult, dto.getVerificationResult());
    }

    @Test
    void testSetters() {
        // Arrange
        AuthRequestOTPDto dto = new AuthRequestOTPDto();

        String fidoAuthenticationRequest = "fido_auth_request";
        String authenticationRequestId = "auth_request_id";
        Date expiration = new Date();
        Long availableRetries = 3L;
        Long totalRetriesAllowed = 5L;
        String authenticationCode = "123456";
        Date created = new Date();
        String status = "pending";
        String verificationResult = "success";

        // Act
        dto.setFidoAuthenticationRequest(fidoAuthenticationRequest);
        dto.setAuthenticationRequestId(authenticationRequestId);
        dto.setExpiration(expiration);
        dto.setAvailableRetries(availableRetries);
        dto.setTotalRetriesAllowed(totalRetriesAllowed);
        dto.setAuthenticationCode(authenticationCode);
        dto.setCreated(created);
        dto.setStatus(status);
        dto.setVerificationResult(verificationResult);

        // Assert
        assertEquals(fidoAuthenticationRequest, dto.getFidoAuthenticationRequest());
        assertEquals(authenticationRequestId, dto.getAuthenticationRequestId());
        assertEquals(expiration, dto.getExpiration());
        assertEquals(availableRetries, dto.getAvailableRetries());
        assertEquals(totalRetriesAllowed, dto.getTotalRetriesAllowed());
        assertEquals(authenticationCode, dto.getAuthenticationCode());
        assertEquals(created, dto.getCreated());
        assertEquals(status, dto.getStatus());
        assertEquals(verificationResult, dto.getVerificationResult());
    }
}