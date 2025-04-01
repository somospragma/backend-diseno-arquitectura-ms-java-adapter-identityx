package co.com.pragma.identityx.config;

import io.swagger.v3.oas.models.info.Info;
import lombok.NoArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
public class OpenApiConfig {

    @Value("${info.app.version}")
    private String infoAppVersion;

    @Value("${info.app.name}")
    private String infoAppName;

    @Value("${info.app.description}")
    private String infoAppDescription;

    public GroupedOpenApi userApi(){
        final String[] packagesToScan = {"co.com.pragma.identityx.entry_points.rest.controller"};
        return GroupedOpenApi
                .builder()
                .group("Adapter for identity x DAON")
                .packagesToScan(packagesToScan)
                .pathsToMatch("/**")
                .addOpenApiCustomizer(statusApiCustomizer())
                .build();
    }

    private OpenApiCustomizer statusApiCustomizer() {
        return openApi -> openApi
                .info(new Info()
                        .title(infoAppName)
                        .description(infoAppDescription)
                        .version(infoAppVersion));
    }

}
