package com.spring.interview.testing.service;

import com.spring.interview.testing.model.User;
import com.spring.interview.testing.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(String id, String username, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        User user = new User(id, username, email);
        return userRepository.save(user);
    }
}
