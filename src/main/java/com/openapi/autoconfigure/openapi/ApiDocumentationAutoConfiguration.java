package com.openapi.autoconfigure.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.stream.Collectors;

@AutoConfiguration
@ConditionalOnProperty(prefix = "app.infra.openapi", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ApiDocProperties.class)
public class ApiDocumentationAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ApiDocumentationAutoConfiguration.class);

    private final ApiDocProperties properties;

    public ApiDocumentationAutoConfiguration(ApiDocProperties properties) {
        this.properties = properties;
        logger.debug("ApiDocumentationAutoConfiguration initialized with title: {}", properties.getTitle());
    }

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        logger.info("Configuring OpenAPI documentation with custom properties");

        OpenAPI openAPI = new OpenAPI()
                .info(buildInfo())
                .components(buildComponents());

        // Add servers if configured
        if (properties.getServers() != null && !properties.getServers().isEmpty()) {
            openAPI.servers(buildServers());
        }

        // Add global tags if configured
        if (properties.getTags() != null && !properties.getTags().isEmpty()) {
            openAPI.tags(buildTags());
        }

        logger.debug("OpenAPI configuration completed for: {}", properties.getTitle());
        return openAPI;
    }

    private Info buildInfo() {
        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion());

        // Add contact information
        ApiDocProperties.Contact contactProps = properties.getContact();
        if (contactProps != null) {
            Contact contact = new Contact()
                    .name(contactProps.getName());
            if (contactProps.getEmail() != null) {
                contact.email(contactProps.getEmail());
            }
            if (contactProps.getUrl() != null) {
                contact.url(contactProps.getUrl());
            }
            info.contact(contact);
        }

        return info;
    }

    private Components buildComponents() {
        Components components = new Components();

        // Add security schemes if security is enabled
        if (properties.getSecurity() != null && properties.getSecurity().isEnabled()) {
            ApiDocProperties.ApiKey apiKey = properties.getSecurity().getApiKey();
            if (apiKey != null && apiKey.isEnabled()) {
                SecurityScheme apiKeyScheme = new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name(apiKey.getHeaderName());
                components.addSecuritySchemes("apiKey", apiKeyScheme);
            }
        }

        return components;
    }

    private List<Server> buildServers() {
        return properties.getServers().stream()
                .map(serverProps -> new Server()
                        .url(serverProps.getUrl())
                        .description(serverProps.getDescription()))
                .collect(Collectors.toList());
    }

    private List<Tag> buildTags() {
        return properties.getTags().stream()
                .map(tagProps -> new Tag()
                        .name(tagProps.getName())
                        .description(tagProps.getDescription()))
                .collect(Collectors.toList());
    }
}
