package com.example.urlshortenerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "email не должен быть пустым") @Email String email,
        @NotBlank(message = "пароль не должен быть пустым") @Size(min = 8, message = "пароль должен состоять минимум из восьми символов") String password
){}
