package com.spring.interview.jdbc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserJdbcRepositoryTest {

    @Autowired
    private UserJdbcRepository repository;

    @BeforeEach
    void setUp() {
        repository.initSchema();
    }

    @Test
    @DisplayName("Should insert single user and query by ID using NamedParameterJdbcTemplate")
    void shouldInsertAndFindUser() {
        var user = new UserJdbcRepository.UserRecord("usr-1", "sourav", "sourav@example.com", "ACTIVE");
        int rows = repository.insert(user);
        assertThat(rows).isEqualTo(1);

        var found = repository.findById("usr-1");
        assertThat(found).isPresent();
        assertThat(found.get().username()).isEqualTo("sourav");
        assertThat(found.get().email()).isEqualTo("sourav@example.com");
    }

    @Test
    @DisplayName("Should perform batch insert on multiple user records")
    void shouldPerformBatchInsert() {
        var users = List.of(
            new UserJdbcRepository.UserRecord("usr-2", "alice", "alice@example.com", "ACTIVE"),
            new UserJdbcRepository.UserRecord("usr-3", "bob", "bob@example.com", "PENDING"),
            new UserJdbcRepository.UserRecord("usr-4", "carol", "carol@example.com", "SUSPENDED")
        );

        int[] updateCounts = repository.batchInsert(users);
        assertThat(updateCounts).hasSize(3);
        assertThat(repository.count()).isGreaterThanOrEqualTo(3);
    }
}
