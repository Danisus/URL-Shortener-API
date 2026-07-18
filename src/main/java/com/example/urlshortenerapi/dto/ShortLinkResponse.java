package com.example.urlshortenerapi.dto;

import com.example.urlshortenerapi.entity.ShortLink;

import java.time.LocalDateTime;

public record ShortLinkResponse(
        Long id,
        String shortCode,
        String originalLink,
        String ownerEmail,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        boolean isActive
) {
    public static ShortLinkResponse from(ShortLink link) {
        return new ShortLinkResponse(
                link.getId(),
                link.getShortCode(),
                link.getOriginalLink(),
                link.getUser().getEmail(),
                link.getCreatedAt(),
                link.getExpiresAt(),
                link.isActive()
        );
    }
}
