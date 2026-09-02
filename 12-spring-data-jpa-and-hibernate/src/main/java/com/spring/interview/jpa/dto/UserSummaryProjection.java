package com.spring.interview.jpa.dto;

public final class UserSummaryProjection {

    // 1. Interface-based Closed Projection
    public interface UserSummaryView {
        Long getId();
        String getUsername();
        String getEmail();
    }

    // 2. High-Performance Java 21 Record Constructor Projection
    public record UserSummaryRecord(Long id, String username, String email) {}
}
