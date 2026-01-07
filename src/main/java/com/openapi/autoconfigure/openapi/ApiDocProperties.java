package com.openapi.autoconfigure.openapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.infra.openapi")
public class ApiDocProperties {

    /**
     * Whether to enable OpenAPI documentation auto-configuration
     */
    private boolean enabled = true;

    /**
     * API title
     */
    private String title = "API Documentation";

    /**
     * API description
     */
    private String description = "API Description";

    /**
     * API version
     */
    private String version = "1.0.0";

    /**
     * Contact information
     */
    private Contact contact = new Contact();

    /**
     * Security configuration
     */
    private Security security = new Security();

    /**
     * Server configurations
     */
    private List<Server> servers;

    /**
     * Global tags for grouping operations
     */
    private List<Tag> tags;

    // Getters and setters

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public static class Contact {
        private String name = "API Support";
        private String email;
        private String url;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Security {
        /**
         * Whether to enable security configuration
         */
        private boolean enabled = false;

        /**
         * API Key security configuration
         */
        private ApiKey apiKey = new ApiKey();

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public ApiKey getApiKey() {
            return apiKey;
        }

        public void setApiKey(ApiKey apiKey) {
            this.apiKey = apiKey;
        }
    }

    public static class ApiKey {
        /**
         * Whether to enable API Key security
         */
        private boolean enabled = true;

        /**
         * Security scheme type
         */
        private String type = "apiKey";

        /**
         * Header name for API Key
         */
        private String headerName = "X-API-Key";

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }
    }

    public static class Server {
        private String url;
        private String description;

        // Getters and setters
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class Tag {
        private String name;
        private String description;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
