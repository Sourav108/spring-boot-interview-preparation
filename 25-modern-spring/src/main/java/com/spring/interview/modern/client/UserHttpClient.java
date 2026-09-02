package com.spring.interview.modern.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Modern Spring 6 declarative HTTP Interface using @HttpExchange.
 */
@HttpExchange("/api/v1/users")
public interface UserHttpClient {

    record RemoteUserDto(String id, String username, String email) {}

    @GetExchange("/{id}")
    RemoteUserDto getUserById(@PathVariable("id") String id);

    @PostExchange
    RemoteUserDto createUser(RemoteUserDto userDto);
}
