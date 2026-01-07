package com.openapi.autoconfigure.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ApiDocumentationAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ApiDocumentationAutoConfiguration.class));

    @Test
    void shouldConfigureOpenAPIWithDefaultProperties() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(io.swagger.v3.oas.models.OpenAPI.class);
            ApiDocProperties properties = context.getBean(ApiDocProperties.class);
            assertThat(properties.isEnabled()).isTrue();
            assertThat(properties.getTitle()).isEqualTo("API Documentation");
        });
    }

    @Test
    void shouldNotConfigureWhenDisabled() {
        contextRunner.withPropertyValues("app.infra.openapi.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(io.swagger.v3.oas.models.OpenAPI.class);
                });
    }

    @Test
    void shouldConfigureWithCustomProperties() {
        contextRunner.withPropertyValues(
                "app.infra.openapi.title=Custom API",
                "app.infra.openapi.description=Custom Description",
                "app.infra.openapi.version=2.0.0",
                "app.infra.openapi.contact.name=Test Support",
                "app.infra.openapi.contact.email=test@example.com"
        ).run(context -> {
            assertThat(context).hasSingleBean(io.swagger.v3.oas.models.OpenAPI.class);
            ApiDocProperties properties = context.getBean(ApiDocProperties.class);
            assertThat(properties.getTitle()).isEqualTo("Custom API");
            assertThat(properties.getDescription()).isEqualTo("Custom Description");
            assertThat(properties.getVersion()).isEqualTo("2.0.0");
            assertThat(properties.getContact().getName()).isEqualTo("Test Support");
            assertThat(properties.getContact().getEmail()).isEqualTo("test@example.com");
        });
    }
}
