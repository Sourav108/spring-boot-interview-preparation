package com.spring.interview.jpa.repository;

import com.spring.interview.jpa.entity.OrderEntity;
import com.spring.interview.jpa.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private Long savedUserId;

    @BeforeEach
    void setUp() {
        var user = new UserEntity("souravsaha", "sourav@enterprise.com", "ACTIVE");
        var order1 = new OrderEntity("ORD-1001", 150.0);
        var order2 = new OrderEntity("ORD-1002", 300.0);

        user.addOrder(order1);
        user.addOrder(order2);

        UserEntity saved = userRepository.save(user);
        savedUserId = saved.getId();
    }

    @Test
    @DisplayName("Should fetch user and child orders eagerly using JOIN FETCH to mitigate N+1 query")
    void shouldFetchUserWithOrdersViaJoinFetch() {
        var userOpt = userRepository.findByIdWithOrders(savedUserId);

        assertThat(userOpt).isPresent();
        var user = userOpt.get();
        assertThat(user.getOrders()).hasSize(2);
        assertThat(user.getOrders().getFirst().getOrderNumber()).isEqualTo("ORD-1001");
    }

    @Test
    @DisplayName("Should query user projections using high-performance Java 21 Record constructors")
    void shouldQueryRecordConstructorProjections() {
        var records = userRepository.findAllUserSummaryRecords();

        assertThat(records).isNotEmpty();
        var record = records.getFirst();
        assertThat(record.username()).isEqualTo("souravsaha");
        assertThat(record.email()).isEqualTo("sourav@enterprise.com");
    }

    @Test
    @DisplayName("Should query using @EntityGraph declarative fetch plans")
    void shouldQueryUsingEntityGraph() {
        var activeUsers = userRepository.findByStatus("ACTIVE");

        assertThat(activeUsers).isNotEmpty();
        assertThat(activeUsers.getFirst().getOrders()).hasSize(2);
    }
}
