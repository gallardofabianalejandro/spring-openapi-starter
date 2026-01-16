# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Initial project setup and configuration

## [1.0.1] - 2026-01-16

### Added
- Soporte para refresco dinámico de propiedades OpenAPI mediante Spring Cloud Config (Video 87).
- `EnvironmentChangeEvent` listener para limpiar automáticamente la caché de SpringDoc al detectar cambios en `app.infra.openapi.*`.
- Inicialización defensiva de objetos `Info`, `Paths` y `Components` en `OpenApiCustomizer` para evitar `NullPointerException` durante regeneraciones de la especificación.
- Uso de reflexión para invocar el método `reset()` de `OpenAPIService`, asegurando compatibilidad entre versiones de SpringDoc.

### Changed
- Eliminación del uso de `@RefreshScope` en el bean `OpenAPI` y `ApiDocProperties` para evitar problemas de serialización con proxies CGLIB.
- Migración de la definición manual del bean `OpenAPI` a una estrategia basada íntegramente en `OpenApiCustomizer` para garantizar instancias limpias tras cada refresco.
- Refactorización de la inyección de `OpenAPIService` utilizando `ObjectProvider` para romper ciclos de dependencia durante la fase de auto-configuración.

### Fixed
- Error de serialización (CGLIB proxy) al intentar servir el JSON de OpenAPI cuando el bean estaba en Refresh Scope.
- `NullPointerException` al acceder a Swagger UI cuando la especificación se regeneraba con componentes nulos.
- Dependencia circular entre `ApiDocumentationAutoConfiguration` y `OpenAPIService`.

## [1.0.0] - 2026-01-01

### Added

- Spring Boot Starter for OpenAPI Documentation and API Key Security
- Auto-configuration for OpenAPI documentation
- API key security implementation
- SpringDoc OpenAPI integration

### Changed

-

### Deprecated

-

### Removed

-

### Fixed

-

### Security

-
