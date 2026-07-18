package com.example.urlshortenerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email не должен быть пустым") @Email String email,
        @NotBlank(message = "пароль не должен быть пустым") String password
){}
