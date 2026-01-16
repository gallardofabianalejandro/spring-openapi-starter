package com.openapi.autoconfigure.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.service.OpenAPIService;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@AutoConfiguration
@ConditionalOnProperty(prefix = "app.infra.openapi", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ApiDocProperties.class)
public class ApiDocumentationAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ApiDocumentationAutoConfiguration.class);

    private final ApiDocProperties properties;

    private final ObjectProvider<OpenAPIService> openAPIServiceProvider;

    public ApiDocumentationAutoConfiguration(ApiDocProperties properties, ObjectProvider<OpenAPIService> openAPIServiceProvider) {
        this.properties = properties;
        this.openAPIServiceProvider = openAPIServiceProvider;
        logger.info("=== ApiDocumentationAutoConfiguration initialized ===");
        logger.info("Current properties - Title: '{}', Description: '{}', Version: '{}'",
                   properties.getTitle(), properties.getDescription(), properties.getVersion());
        logger.info("Contact - Name: '{}', Email: '{}', URL: '{}'",
                   properties.getContact() != null ? properties.getContact().getName() : "null",
                   properties.getContact() != null ? properties.getContact().getEmail() : "null",
                   properties.getContact() != null ? properties.getContact().getUrl() : "null");
        logger.info("Security enabled: {}", properties.getSecurity() != null ? properties.getSecurity().isEnabled() : "null");
        logger.info("====================================================");
    }

    /**
     * Listener to clear SpringDoc cache when properties are refreshed
     */
    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent(EnvironmentChangeEvent event) {
        OpenAPIService openAPIService = openAPIServiceProvider.getIfAvailable();
        if (openAPIService != null && event.getKeys().stream().anyMatch(key -> key.startsWith("app.infra.openapi"))) {
            logger.info("=== [STARTER] OpenAPI properties changed, attempting to reset SpringDoc cache ===");
            try {
                // Use reflection to call reset() to avoid compilation issues if version varies
                try {
                    Method resetMethod = openAPIService.getClass().getMethod("reset");
                    resetMethod.invoke(openAPIService);
                    logger.info("=== [STARTER] SpringDoc cache reset via reset() ===");
                } catch (NoSuchMethodException e) {
                    // Fallback to searching for cache map fields
                    boolean cleared = false;
                    for (Field field : openAPIService.getClass().getDeclaredFields()) {
                        if (field.getName().toLowerCase().contains("cache")) {
                            field.setAccessible(true);
                            Object cache = field.get(openAPIService);
                            if (cache instanceof Map) {
                                ((Map<?, ?>) cache).clear();
                                logger.info("=== [STARTER] SpringDoc cache cleared via field: {} ===", field.getName());
                                cleared = true;
                            }
                        }
                    }
                    if (!cleared) {
                        logger.warn("=== [STARTER] Could not find reset() method or cache field in OpenAPIService ===");
                    }
                }
            } catch (Exception e) {
                logger.error("=== [STARTER] Error resetting SpringDoc cache: {} ===", e.getMessage());
            }
        }
    }

    /**
     * Customizer to update OpenAPI info from refreshed properties.
     * We don't use @RefreshScope here because ApiDocProperties is a singleton
     * that is automatically updated by ConfigurationPropertiesRebinder.
     */
    @Bean
    public OpenApiCustomizer openApiInfoCustomizer() {
        return openApi -> {
            logger.info("=== [STARTER] OpenApiInfoCustomizer EXECUTING ===");
            
            // Ensure essential objects are initialized to prevent NPEs
            if (openApi.getInfo() == null) {
                openApi.setInfo(new Info());
                logger.info("=== [STARTER] Initialized null Info ===");
            }
            if (openApi.getPaths() == null) {
                openApi.setPaths(new Paths());
                logger.info("=== [STARTER] Initialized null Paths ===");
            }
            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
                logger.info("=== [STARTER] Initialized null Components ===");
            }

            Info info = openApi.getInfo();
            
            // Update title, description, and version from properties
            if (properties.getTitle() != null) {
                info.setTitle(properties.getTitle());
            }
            if (properties.getDescription() != null) {
                info.setDescription(properties.getDescription());
            }
            if (properties.getVersion() != null) {
                info.setVersion(properties.getVersion());
            }

            // Update contact information
            if (properties.getContact() != null) {
                Contact contact = info.getContact();
                if (contact == null) {
                    contact = new Contact();
                    info.setContact(contact);
                }
                if (properties.getContact().getName() != null) {
                    contact.setName(properties.getContact().getName());
                }
                if (properties.getContact().getEmail() != null) {
                    contact.setEmail(properties.getContact().getEmail());
                }
                if (properties.getContact().getUrl() != null) {
                    contact.setUrl(properties.getContact().getUrl());
                }
            }

            logger.info("=== [STARTER] OpenApiInfoCustomizer COMPLETED - Title: '{}', Description: '{}' ===", 
                       info.getTitle(), info.getDescription());
        };
    }
}
