package com.spring.interview.testing.repository;

import com.spring.interview.testing.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> userDb = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userDb.get(id));
    }

    @Override
    public User save(User user) {
        userDb.put(user.id(), user);
        return user;
    }
}
