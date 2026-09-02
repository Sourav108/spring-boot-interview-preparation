package com.spring.interview.rest.service;

import com.spring.interview.rest.dto.UserRequestDto;
import com.spring.interview.rest.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryUserService {

    private final Map<String, UserResponseDto> userStore = new ConcurrentHashMap<>();

    public UserResponseDto createUser(UserRequestDto request) {
        String id = UUID.randomUUID().toString();
        UserResponseDto created = new UserResponseDto(
            id,
            request.username(),
            request.email(),
            request.role(),
            Instant.now().toString()
        );
        userStore.put(id, created);
        return created;
    }

    public Optional<UserResponseDto> getUserById(String id) {
        return Optional.ofNullable(userStore.get(id));
    }

    public List<UserResponseDto> getAllUsers(int offset, int limit) {
        return userStore.values().stream()
            .skip(offset)
            .limit(limit)
            .toList();
    }

    public Optional<UserResponseDto> updateUser(String id, UserRequestDto request) {
        if (!userStore.containsKey(id)) {
            return Optional.empty();
        }
        UserResponseDto updated = new UserResponseDto(
            id,
            request.username(),
            request.email(),
            request.role(),
            userStore.get(id).createdAt()
        );
        userStore.put(id, updated);
        return Optional.of(updated);
    }

    public boolean deleteUser(String id) {
        return userStore.remove(id) != null;
    }

    public void clear() {
        userStore.clear();
    }
}
