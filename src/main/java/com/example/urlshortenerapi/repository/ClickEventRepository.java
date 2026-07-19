package com.example.urlshortenerapi.repository;

import com.example.urlshortenerapi.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    long countClickEventByShortLinkId(Long shortLinkId);
}