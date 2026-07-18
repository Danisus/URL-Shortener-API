package com.example.urlshortenerapi.controller;

import com.example.urlshortenerapi.dto.AuthResponse;
import com.example.urlshortenerapi.dto.LoginRequest;
import com.example.urlshortenerapi.dto.RegisterRequest;
import com.example.urlshortenerapi.entity.User;
import com.example.urlshortenerapi.exception.EmailAlreadyExistsException;
import com.example.urlshortenerapi.repository.UserRepository;
import com.example.urlshortenerapi.security.CustomUserDetailsService;
import com.example.urlshortenerapi.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request){
        if (userRepository.findUserByEmail(request.email()).isPresent())
            throw new EmailAlreadyExistsException("пользователь с такой почтой уже существует");

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(encoder.encode(request.password()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
