package co.com.pragma.identityx.driven_adapters.secrets_manager;

import co.com.pragma.identityx.driven_adapters.secret_manager.dto.KeysDto;
import co.com.pragma.identityx.driven_adapters.secret_manager.service.SecretManagerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class SecretManagerServiceImplementTest {

    @InjectMocks
    private SecretManagerServiceImpl secretManagerService;

    @Mock
    private SecretsManagerClient secretsManagerClient;

    @Test
    void getSecret_IntegrationTest() {
        // Arrange
        String jsonResponse = "{\"public\":\"MOCK_PUBLIC_KEY\",\"PrivateKey\":\"MOCK_PRIVATE_KEY\",\"ApiKey\":\"MOCK_API_KEY\"}";

        GetSecretValueResponse mockResponse = GetSecretValueResponse.builder()
                .secretString(jsonResponse)
                .build();

        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
                .thenReturn(mockResponse);

        // Act
        KeysDto result = secretManagerService.getSecret("SECRET_NAME");

        // Assert
        assertNotNull(result);
        assertEquals("MOCK_PUBLIC_KEY", result.getPublicKey());
        assertEquals("MOCK_PRIVATE_KEY", result.getPrivateKey());
        assertEquals("MOCK_API_KEY", result.getApiKey());
    }

    @Test
    void getSecret_NotFoundTest() {
        // Arrange
        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
                .thenThrow(ResourceNotFoundException.builder().build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            secretManagerService.getSecret("SECRET_NAME");
        });

        // Assert
        assertEquals("Secret not found: SECRET_NAME", exception.getMessage());
    }


}
