package com.spring.interview.testing.unit;

import com.spring.interview.testing.model.User;
import com.spring.interview.testing.repository.UserRepository;
import com.spring.interview.testing.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserById() {
        var user = new User("usr-1", "alice", "alice@example.com");
        when(userRepository.findById("usr-1")).thenReturn(Optional.of(user));

        var result = userService.findById("usr-1");

        assertThat(result).isPresent();
        assertThat(result.get().username()).isEqualTo("alice");
        verify(userRepository).findById("usr-1");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating user with blank username")
    void shouldThrowWhenUsernameBlank() {
        assertThatThrownBy(() -> userService.createUser("usr-2", "", "email@test.com"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username cannot be empty");
    }

    @Test
    @DisplayName("Should save and return user when valid inputs provided")
    void shouldCreateUserSuccessfully() {
        var user = new User("usr-3", "bob", "bob@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);

        var created = userService.createUser("usr-3", "bob", "bob@example.com");

        assertThat(created.username()).isEqualTo("bob");
        verify(userRepository).save(any(User.class));
    }
}
