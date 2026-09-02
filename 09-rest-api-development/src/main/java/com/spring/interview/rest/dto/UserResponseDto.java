package com.spring.interview.rest.dto;

public record UserResponseDto(
    String id,
    String username,
    String email,
    String role,
    String createdAt
) {}
