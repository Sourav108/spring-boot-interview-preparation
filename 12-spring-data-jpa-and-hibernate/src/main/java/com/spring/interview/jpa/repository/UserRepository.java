package com.spring.interview.jpa.repository;

import com.spring.interview.jpa.dto.UserSummaryProjection.UserSummaryRecord;
import com.spring.interview.jpa.dto.UserSummaryProjection.UserSummaryView;
import com.spring.interview.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    // 1. Eager JOIN FETCH query mitigating N+1 query problem
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.orders WHERE u.id = :id")
    Optional<UserEntity> findByIdWithOrders(@Param("id") Long id);

    // 2. @EntityGraph declarative fetch plan
    @EntityGraph(attributePaths = {"orders"})
    List<UserEntity> findByStatus(String status);

    // 3. Interface Projection
    List<UserSummaryView> findProjectedByStatus(String status);

    // 4. Java 21 Record Constructor Projection (Zero dirty-checking overhead)
    @Query("SELECT new com.spring.interview.jpa.dto.UserSummaryProjection$UserSummaryRecord(u.id, u.username, u.email) FROM UserEntity u")
    List<UserSummaryRecord> findAllUserSummaryRecords();
}
