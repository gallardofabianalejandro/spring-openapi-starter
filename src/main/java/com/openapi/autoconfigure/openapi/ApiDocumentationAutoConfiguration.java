package com.openapi.autoconfigure.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoConfiguration
@ConditionalOnProperty(prefix = "app.infra.openapi", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ApiDocProperties.class)
@RefreshScope
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

    /**
     * Provides global API responses that can be reused across all endpoints
     */
    @Bean
    @RefreshScope
    public OpenApiCustomizer globalApiResponsesCustomizer() {
        return openApi -> {
            Components components = openApi.getComponents();
            if (components == null) {
                components = new Components();
                openApi.setComponents(components);
            }

            // Initialize responses map if null
            if (components.getResponses() == null) {
                components.setResponses(new HashMap<>());
            }

            // Add common global responses
            Map<String, ApiResponse> globalResponses = new HashMap<>();

            // Success responses
            globalResponses.put("Success",
                new ApiResponse().description("Operation completed successfully"));

            globalResponses.put("Created",
                new ApiResponse().description("Resource created successfully"));

            globalResponses.put("NoContent",
                new ApiResponse().description("Operation completed, no content returned"));

            // Error responses
            globalResponses.put("BadRequest",
                new ApiResponse().description("Invalid input data or validation error"));

            globalResponses.put("Unauthorized",
                new ApiResponse().description("Authentication required or invalid credentials"));

            globalResponses.put("Forbidden",
                new ApiResponse().description("Access denied to this resource"));

            globalResponses.put("NotFound",
                new ApiResponse().description("Requested resource not found"));

            globalResponses.put("Conflict",
                new ApiResponse().description("Resource conflict or already exists"));

            globalResponses.put("InternalServerError",
                new ApiResponse().description("Internal server error occurred"));

            // Add all global responses
            Components finalComponents = components;
            globalResponses.forEach((key, response) ->
                finalComponents.getResponses().putIfAbsent(key, response));

            logger.debug("Added {} global API responses to OpenAPI configuration",
                        globalResponses.size());
        };
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
