package org.angel.java.notesapp.shared.infrastructure.documentation.openapi.configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI notesAppOpenApi() {
        var openApi = new OpenAPI();

        openApi
                .info(new Info()
                        .title("Notes App API")
                        .description("REST API documentation for the Notes Application backend.")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Notes App README")
                        .url("https://github.com/hirelens-challenges/Lavado-bebef3/blob/develop/README.md"));

        final String securityScheme = "bearerAuth";

        openApi
                .addSecurityItem(new SecurityRequirement()
                        .addList(securityScheme))
                .components(new Components()
                        .addSecuritySchemes(securityScheme, new SecurityScheme()
                                .name(securityScheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));

        return openApi;
    }
}
