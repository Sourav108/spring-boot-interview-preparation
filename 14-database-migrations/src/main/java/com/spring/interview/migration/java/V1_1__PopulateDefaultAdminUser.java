package com.spring.interview.migration.java;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;

/**
 * Java-based Flyway migration demonstrating programmatic data seeding.
 */
public class V1_1__PopulateDefaultAdminUser extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        String sql = "INSERT INTO users (id, username, email, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = context.getConnection().prepareStatement(sql)) {
            statement.setString(1, "usr-admin-seed");
            statement.setString(2, "admin");
            statement.setString(3, "admin@system.local");
            statement.setString(4, "ACTIVE");
            statement.executeUpdate();
        }
    }
}
