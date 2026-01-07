# Spring OpenAPI Starter

Un starter de Spring Boot que configura automáticamente la documentación OpenAPI (Swagger) con propiedades personalizables y soporte para seguridad API Key.

## Características

- ✅ **Configuración automática** de OpenAPI/Swagger
- ✅ **Propiedades personalizables** a través de `application.yaml`
- ✅ **Soporte para API Key security**
- ✅ **Información de contacto** configurable
- ✅ **Servidores múltiples** y tags globales
- ✅ **Prioridad sobre SpringDoc** con `@Primary`
- ✅ **Desactivación opcional** del starter

## Registro de Cambios

Consulta el [CHANGELOG.md](CHANGELOG.md) para ver el historial completo de versiones, nuevas características, cambios, correcciones y mejoras realizadas en el proyecto.

## Instalación

### Opción 1: Dependencia local (desarrollo)
```xml
<dependency>
    <groupId>com.openapi</groupId>
    <artifactId>spring-openapi-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Opción 2: Publicar a repositorio
1. Instalar localmente: `mvn clean install`
2. Publicar a tu repositorio corporativo
3. Agregar la dependencia en tus proyectos

## Configuración

### Configuración básica
```yaml
app:
  infra:
    openapi:
      title: "Mi API"
      description: "Descripción de mi API"
      version: "1.0.0"
```

### Configuración completa
```yaml
app:
  infra:
    openapi:
      enabled: true  # Deshabilitar con false
      title: "Mi API Rest"
      description: "API para gestión de recursos"
      version: "1.0.0"
      contact:
        name: "Equipo de Desarrollo"
        email: "dev@company.com"
        url: "https://company.com"
      security:
        enabled: true
        api-key:
          enabled: true
          header-name: "X-API-Key"
          type: "apiKey"
      servers:
        - url: "https://api.company.com"
          description: "Producción"
        - url: "https://staging.company.com"
          description: "Staging"
      tags:
        - name: "Usuarios"
          description: "Operaciones con usuarios"
```

## Uso

### En cualquier aplicación Spring Boot

1. **Agregar dependencia**
2. **Configurar propiedades** en `application.yaml`
3. **Acceder a Swagger UI**: `http://localhost:8080/swagger-ui.html`
4. **Acceder a OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Deshabilitar el starter

```yaml
app:
  infra:
    openapi:
      enabled: false
```

### Sobrescribir configuración

Si necesitas personalización adicional, puedes crear tu propio bean `@Primary`:

```java
@Configuration
public class CustomOpenApiConfig {

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        // Tu configuración personalizada aquí
        return new OpenAPI()
            .info(new Info().title("Mi Título Personalizado"));
            // ... más configuraciones
    }
}
```

## Propiedades

| Propiedad | Descripción | Default |
|-----------|-------------|---------|
| `app.infra.openapi.enabled` | Habilita/deshabilita el starter | `true` |
| `app.infra.openapi.title` | Título de la API | `"API Documentation"` |
| `app.infra.openapi.description` | Descripción de la API | `"API Description"` |
| `app.infra.openapi.version` | Versión de la API | `"1.0.0"` |
| `app.infra.openapi.contact.name` | Nombre del contacto | `"API Support"` |
| `app.infra.openapi.contact.email` | Email del contacto | - |
| `app.infra.openapi.contact.url` | URL del contacto | - |
| `app.infra.openapi.security.enabled` | Habilita seguridad | `false` |
| `app.infra.openapi.security.api-key.enabled` | Habilita API Key | `true` |
| `app.infra.openapi.security.api-key.header-name` | Nombre del header | `"X-API-Key"` |

## Ejemplos de uso

### Microservicio básico
```yaml
app:
  infra:
    openapi:
      title: "User Service API"
      description: "API para gestión de usuarios"
      version: "2.1.0"
      contact:
        name: "User Team"
        email: "user-team@company.com"
```

### Con seguridad API Key
```yaml
app:
  infra:
    openapi:
      title: "Secure API"
      description: "API con autenticación"
      security:
        enabled: true
      contact:
        name: "Security Team"
        email: "security@company.com"
```

## Compatibilidad

- **Spring Boot**: 3.2.0+
- **Java**: 17+
- **SpringDoc OpenAPI**: 2.7.0

## Soporte

Para soporte técnico, contactar al equipo de infraestructura.
