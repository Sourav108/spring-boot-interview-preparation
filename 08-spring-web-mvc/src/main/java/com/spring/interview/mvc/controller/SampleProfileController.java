package com.spring.interview.mvc.controller;

import com.spring.interview.mvc.resolver.CurrentUser;
import com.spring.interview.mvc.resolver.CurrentUserArgumentResolver.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class SampleProfileController {

    @GetMapping("/me")
    public Map<String, Object> getCurrentProfile(@CurrentUser UserPrincipal principal) {
        return Map.of(
            "userId", principal.userId(),
            "email", principal.email(),
            "role", principal.role(),
            "status", "ACTIVE"
        );
    }
}
