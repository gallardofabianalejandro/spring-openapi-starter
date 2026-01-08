package com.openapi.autoconfigure.openapi;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotaciones compuestas genéricas reutilizables en cualquier microservicio.
 * Estas anotaciones están disponibles automáticamente para todos los proyectos que usen el starter.
 *
 * Para personalizaciones específicas (ej: formato de teléfono por país), los microservicios
 * pueden crear sus propias anotaciones que sobrescriban estas genéricas.
 */
public class ApiAnnotations {

    /**
     * Respuestas API estándar para operaciones CRUD (GET)
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    public @interface StandardApiResponses {}

    /**
     * Respuestas API para operaciones de creación (POST)
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Resource created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Resource already exists")
    })
    public @interface CreateApiResponses {}

    /**
     * Respuestas API para operaciones de actualización (PUT)
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resource updated successfully"),
        @ApiResponse(responseCode = "304", description = "No changes made"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    public @interface UpdateApiResponses {}

    /**
     * Respuestas API para operaciones de eliminación (DELETE)
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resource deleted successfully"),
        @ApiResponse(responseCode = "304", description = "Resource could not be deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    public @interface DeleteApiResponses {}

    /**
     * Parámetro genérico para ID (puede ser sobrescrito por microservicios específicos)
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "Resource identifier", example = "123", required = true)
    public @interface IdParam {}

    /**
     * Parámetro genérico para datos de entrada (puede ser sobrescrito)
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "Resource data", required = true)
    public @interface DataParam {}

    /**
     * Parámetro para número de teléfono genérico (internacional)
     * Los microservicios pueden sobrescribir con formatos específicos por país
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "Phone number (international format)",
               example = "+1234567890",
               required = true)
    public @interface PhoneNumberParam {}

    /**
     * Parámetro específico para número de móvil (Argentina - 10 dígitos)
     * Puede ser sobrescrito por otros países con sus formatos específicos
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "Customer's mobile number (10 digits)",
               example = "1234567890",
               required = true)
    public @interface MobileNumberParam {}

    /**
     * Parámetro específico para datos de cliente
     * Puede ser sobrescrito según las necesidades del microservicio
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "Customer information with account details",
               required = true)
    public @interface CustomerDataParam {}

    /**
     * Parámetro específico para ID de cliente
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "Customer ID",
               example = "12345",
               required = true)
    public @interface CustomerIdParam {}
}
