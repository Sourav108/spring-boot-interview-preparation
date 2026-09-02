package com.spring.interview.config.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class DatabasePoolPropertiesTest {

    @Configuration
    @EnableConfigurationProperties(DatabasePoolProperties.class)
    static class TestConfig {}

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(TestConfig.class);

    @Test
    @DisplayName("Should bind valid properties successfully to immutable Java 21 Record")
    void shouldBindValidProperties() {
        contextRunner
            .withPropertyValues(
                "app.database.jdbc-url=jdbc:postgresql://localhost:5432/orders",
                "app.database.username=postgres",
                "app.database.password=secret_password",
                "app.database.max-pool-size=25",
                "app.database.connection-timeout-ms=15000"
            )
            .run(context -> {
                assertThat(context).hasSingleBean(DatabasePoolProperties.class);
                var props = context.getBean(DatabasePoolProperties.class);

                assertThat(props.jdbcUrl()).isEqualTo("jdbc:postgresql://localhost:5432/orders");
                assertThat(props.username()).isEqualTo("postgres");
                assertThat(props.password()).isEqualTo("secret_password");
                assertThat(props.maxPoolSize()).isEqualTo(25);
                assertThat(props.connectionTimeoutMs()).isEqualTo(15000L);
            });
    }

    @Test
    @DisplayName("Should fail startup when validation constraints are violated (e.g. blank URL or pool size < 1)")
    void shouldFailOnValidationViolation() {
        contextRunner
            .withPropertyValues(
                "app.database.jdbc-url=",
                "app.database.username=postgres",
                "app.database.password=secret",
                "app.database.max-pool-size=0" // Fails @Min(1)
            )
            .run(context -> {
                assertThat(context).hasFailed();
            });
    }
}
