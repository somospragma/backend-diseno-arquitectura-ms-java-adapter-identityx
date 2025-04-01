package co.com.pragma.identityx.driven_adapters.identityx.api.feign;

import feign.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.security.KeyStore;

import static org.assertj.core.api.Assertions.assertThat;

class FeignClientConfigTest {

    @InjectMocks
    private FeignClientConfig feignClientConfig;

    @Mock
    KeyStore trustStore;

    @BeforeEach
    void setUp() {
        feignClientConfig = new FeignClientConfig(trustStore);
    }

    @ParameterizedTest
    @CsvSource({
            "local, 10.0.0.1:8080",
            "dev, 10.0.0.1:text",
            "dev, :8080",
            "dev, invalid",
            "dev, "
    })
    void shouldReturnDefaultClient(String environment, String proxyUrl) throws Exception{
        // Arrange
        ReflectionTestUtils.setField(feignClientConfig, "environment", environment);
        ReflectionTestUtils.setField(feignClientConfig, "proxyHost", proxyUrl);

        // Act
        Client client = feignClientConfig.feignClient();

        // Assert
        assertThat(client).isInstanceOf(Client.Default.class);
    }

    @Test
    void shouldReturnProxiedClientWhenValidProxyConfig() throws Exception{
        // Arrange
        ReflectionTestUtils.setField(feignClientConfig, "environment", "dev");
        ReflectionTestUtils.setField(feignClientConfig, "proxyHost", "proxy:8080");

        // Act
        Client client = feignClientConfig.feignClient();

        // Assert
        assertThat(client).isInstanceOf(Client.Proxied.class);
    }

    @ParameterizedTest
    @CsvSource({
            "local, 10.0.0.1:8080",
            "dev, 10.0.0.1:text",
            "dev, :8080",
            "dev, invalid",
            "dev, ",
            "dev, proxy:8080"
    })
    void shouldReturnRestTemplate(String environment, String proxyUrl) throws Exception{
        // Arrange
        ReflectionTestUtils.setField(feignClientConfig, "environment", environment);
        ReflectionTestUtils.setField(feignClientConfig, "proxyHost", proxyUrl);

        // Act
        RestTemplate restTemplate = feignClientConfig.restTemplate();

        // Assert
        assertThat(restTemplate).isInstanceOf(RestTemplate.class);
    }
}