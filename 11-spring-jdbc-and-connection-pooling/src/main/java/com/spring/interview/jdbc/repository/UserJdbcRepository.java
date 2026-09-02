package com.spring.interview.jdbc.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Production repository demonstrating NamedParameterJdbcTemplate, RowMapper, and batch updates.
 */
@Repository
public class UserJdbcRepository {

    public record UserRecord(String id, String username, String email, String status) {}

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<UserRecord> USER_ROW_MAPPER = (rs, rowNum) -> new UserRecord(
        rs.getString("id"),
        rs.getString("username"),
        rs.getString("email"),
        rs.getString("status")
    );

    public void initSchema() {
        jdbcTemplate.getJdbcTemplate().execute("""
            CREATE TABLE IF NOT EXISTS users (
                id VARCHAR(64) PRIMARY KEY,
                username VARCHAR(100) NOT NULL,
                email VARCHAR(150) NOT NULL,
                status VARCHAR(50) NOT NULL
            )
        """);
    }

    public Optional<UserRecord> findById(String id) {
        String sql = "SELECT id, username, email, status FROM users WHERE id = :id";
        List<UserRecord> results = jdbcTemplate.query(sql, Map.of("id", id), USER_ROW_MAPPER);
        return results.stream().findFirst();
    }

    public int insert(UserRecord user) {
        String sql = "INSERT INTO users (id, username, email, status) VALUES (:id, :username, :email, :status)";
        return jdbcTemplate.update(sql, Map.of(
            "id", user.id(),
            "username", user.username(),
            "email", user.email(),
            "status", user.status()
        ));
    }

    public int[] batchInsert(List<UserRecord> users) {
        String sql = "INSERT INTO users (id, username, email, status) VALUES (:id, :username, :email, :status)";
        return jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(users));
    }

    public int count() {
        Integer total = jdbcTemplate.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        return total != null ? total : 0;
    }
}
