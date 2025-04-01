package co.com.pragma.identityx.driven_adapters.secret_manager.service;

import co.com.pragma.identityx.driven_adapters.secret_manager.dto.KeysDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

import java.util.Objects;

@Slf4j
@Service
public class SecretManagerServiceImpl implements SecretManagerService {
    private final SecretsManagerClient secretsManagerClient;

    public SecretManagerServiceImpl(SecretsManagerClient secretsManagerClient){
        this.secretsManagerClient = secretsManagerClient;
    }

    @Override
    public KeysDto getSecret(String secretName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);

            KeysDto keysDto = objectMapper.readValue(response.secretString(), KeysDto.class);
            if (Objects.nonNull(keysDto.getPrivateKey()) && !keysDto.getPrivateKey().isBlank())
                log.info("private key imported successfully");
            if (Objects.nonNull(keysDto.getPublicKey()) && !keysDto.getPublicKey().isBlank())
                log.info("public key imported successfully");
            if (Objects.nonNull(keysDto.getApiKey()) && !keysDto.getApiKey().isBlank())
                log.info("api key imported successfully");
            return keysDto;
        } catch (ResourceNotFoundException e) {
            log.error("secret {} not found",secretName);
            throw new RuntimeException("Secret not found: "+secretName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
