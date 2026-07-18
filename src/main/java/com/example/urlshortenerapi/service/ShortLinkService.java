package com.example.urlshortenerapi.service;

import com.example.urlshortenerapi.dto.CreateShortLinkDto;
import com.example.urlshortenerapi.dto.ShortLinkResponse;
import com.example.urlshortenerapi.entity.ShortLink;
import com.example.urlshortenerapi.entity.User;
import com.example.urlshortenerapi.exception.LinkExpiredException;
import com.example.urlshortenerapi.exception.LinkNotFoundException;
import com.example.urlshortenerapi.repository.ShortLinkRepository;
import com.example.urlshortenerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortLinkService {
    private final ShortLinkRepository repository;
    private final ClickEventService clickEventService;
    private final UserRepository userRepository;

    @Value(value = "${app.base-url}")
    String startLink;

    public String createLink(CreateShortLinkDto createShortLinkDto){
        int lifespanInDays = 1;
        int maxAttempts = 5;
        int characterCount = 7;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("пользователь не найден"));

        for (int i = 0; i < maxAttempts; i++) {
            try {
                ShortLink link = ShortLink.builder()
                        .shortCode(generateCode(characterCount))
                        .originalLink(createShortLinkDto.getUrl())
                        .createdAt(LocalDateTime.now())
                        .user(user)
                        .expiresAt(LocalDateTime.now().plusDays(lifespanInDays))
                        .isActive(true)
                        .build();
                repository.save(link);
                return startLink + link.getShortCode();
            } catch (DataIntegrityViolationException e) {
                log.warn("Collision or constraint violation on attempt {}: {}", i, e.getMessage());
            }
        }
        throw new RuntimeException("Не удалось сгенерировать уникальный код");
    }

    public List<ShortLinkResponse> findAll(){
        return repository.findAll().stream().map(ShortLinkResponse::from).collect(Collectors.toList());
    }

    public ShortLinkResponse findById(long id){
        ShortLink link = repository.findById(id).orElseThrow(() -> new LinkNotFoundException("ссылка не найдена"));
        return ShortLinkResponse.from(link);
    }

    public ShortLink findByShortCode(String shortCode, String ipAddress, String referrer){
        ShortLink link = repository.findByShortCode(shortCode).orElseThrow(() -> new LinkNotFoundException("ссылка не найдена"));
        if (!link.isActive() || link.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new LinkExpiredException("время жизни ссылки истекло");
        clickEventService.recordClick(link, ipAddress, referrer);
        return link;
    }

    private String generateCode(int length){
        String allSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            builder.append(allSymbols.charAt(random.nextInt(allSymbols.length())));
        }
        return builder.toString();
    }
}

