package co.com.pragma.identityx.driven_adapters.secret_manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeysDto {
    @JsonProperty("public")
    private String publicKey;
    @JsonProperty("PrivateKey")
    private String privateKey;
    @JsonProperty("ApiKey")
    private String apiKey;
}
