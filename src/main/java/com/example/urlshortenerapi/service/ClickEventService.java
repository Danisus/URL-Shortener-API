package com.example.urlshortenerapi.service;

import com.example.urlshortenerapi.entity.ClickEvent;
import com.example.urlshortenerapi.entity.ShortLink;
import com.example.urlshortenerapi.repository.ClickEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClickEventService {
    private final ClickEventRepository repository;

    public void recordClick(ShortLink link, String ipAddress, String referrer) {
        repository.save(ClickEvent.builder()
                .shortLink(link)
                .clickedAt(LocalDateTime.now())
                .ipAddress(ipAddress)
                .referrer(referrer)
                .build());
    }
}
