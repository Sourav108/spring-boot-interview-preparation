package com.spring.interview.testing.slice;

import com.spring.interview.testing.controller.UserController;
import com.spring.interview.testing.model.User;
import com.spring.interview.testing.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserSliceTest {

    @Autowired
    private MockMvc mockMvc;

    // Spring Boot 3.4+ standard @MockitoBean replaces deprecated @MockBean
    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should return 200 OK and user details when user exists")
    void shouldReturnUserWhenExists() throws Exception {
        when(userService.findById("usr-100"))
            .thenReturn(Optional.of(new User("usr-100", "alice", "alice@example.com")));

        mockMvc.perform(get("/api/users/usr-100"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("usr-100"))
            .andExpect(jsonPath("$.username").value("alice"))
            .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @DisplayName("Should return 404 Not Found when user does not exist")
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.findById("usr-999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/usr-999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 200 OK on successful user creation")
    void shouldCreateUser() throws Exception {
        when(userService.createUser("usr-200", "bob", "bob@example.com"))
            .thenReturn(new User("usr-200", "bob", "bob@example.com"));

        String requestBody = """
            {
                "id": "usr-200",
                "username": "bob",
                "email": "bob@example.com"
            }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("usr-200"))
            .andExpect(jsonPath("$.username").value("bob"));
    }
}
