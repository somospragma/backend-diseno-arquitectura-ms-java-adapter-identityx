package co.com.pragma.identityx.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfigTest {

    private OpenApiConfig openApiConfig;
    @BeforeEach
    void setUp() {

        openApiConfig = new OpenApiConfig();
    }

    @Test
    void userApi() {
        GroupedOpenApi groupedOpenApi = openApiConfig.userApi();
        Assertions.assertNotNull(groupedOpenApi);
    }

}
