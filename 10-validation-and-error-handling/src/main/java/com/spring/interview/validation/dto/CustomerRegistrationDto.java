package com.spring.interview.validation.dto;

import com.spring.interview.validation.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRegistrationDto(
    @NotBlank(message = "Customer name must not be blank")
    @Size(min = 2, max = 50, message = "Customer name must be between 2 and 50 characters")
    String name,

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email format")
    String email,

    @NotBlank(message = "Phone number must not be blank")
    @ValidPhoneNumber
    String phoneNumber
) {}
