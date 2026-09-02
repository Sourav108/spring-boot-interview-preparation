package com.spring.interview.testing.repository;

import com.spring.interview.testing.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    User save(User user);
}
