package com.example.urlshortenerapi.repository;

import com.example.urlshortenerapi.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    Optional<ShortLink> findByShortCode(String shortCode);

    boolean existsShortLinkByShortCode(String shortCode);
}
