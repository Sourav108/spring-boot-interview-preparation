package com.spring.interview.migration;

import com.spring.interview.migration.callback.AuditMigrationCallback;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FlywayMigrationIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuditMigrationCallback callback;

    @Test
    @DisplayName("Should execute SQL and Java migrations and verify schema state")
    void shouldExecuteFlywayMigrations() {
        MigrationInfo[] appliedMigrations = flyway.info().applied();
        assertThat(appliedMigrations).isNotEmpty();

        // Verify seeded data from V1_1 Java migration
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username = 'admin'",
            Integer.class
        );
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Should invoke Flyway lifecycle callback events")
    void shouldInvokeCallbackEvents() {
        assertThat(callback.getObservedEvents()).isNotEmpty();
    }
}
