package com.spring.interview.config.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/**
 * Immutable Java 21 Record with @ConfigurationProperties and Jakarta Bean Validation.
 */
@Validated
@ConfigurationProperties(prefix = "app.database")
public record DatabasePoolProperties(
    @NotBlank(message = "jdbcUrl must not be blank")
    String jdbcUrl,

    @NotBlank(message = "username must not be blank")
    String username,

    @NotBlank(message = "password must not be blank")
    String password,

    @Min(value = 1, message = "maxPoolSize must be at least 1")
    @Max(value = 100, message = "maxPoolSize cannot exceed 100")
    @DefaultValue("10")
    int maxPoolSize,

    @NotNull(message = "connectionTimeoutMs must be specified")
    @DefaultValue("30000")
    Long connectionTimeoutMs
) {}
