package co.com.pragma.identityx.driven_adapters.secrets_manager;

import co.com.pragma.identityx.driven_adapters.secret_manager.config.SecretManagerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import static org.assertj.core.api.Assertions.assertThat;

class SecretManagerConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    void testClientSecretManager() {
        this.contextRunner
                .withUserConfiguration(SecretManagerConfig.class)
                .run(context -> {
                    SecretsManagerClient secretsManagerClient = context.getBean(SecretsManagerClient.class);
                    assertThat(secretsManagerClient).isNotNull();
                    assertThat("us-east-1").isEqualTo(Region.US_EAST_1.toString());
                });
    }
}
