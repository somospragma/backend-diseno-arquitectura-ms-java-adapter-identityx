package co.com.pragma.identityx.driven_adapters.secret_manager.service;

import co.com.pragma.identityx.driven_adapters.secret_manager.dto.KeysDto;

public interface SecretManagerService {
    KeysDto getSecret(String secretName);
}
