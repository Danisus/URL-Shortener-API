package com.example.urlshortenerapi.service;

import com.example.urlshortenerapi.dto.CountByShortLinkIdDto;
import com.example.urlshortenerapi.entity.ClickEvent;
import com.example.urlshortenerapi.entity.ShortLink;
import com.example.urlshortenerapi.exception.LinkNotFoundException;
import com.example.urlshortenerapi.repository.ClickEventRepository;
import com.example.urlshortenerapi.repository.ShortLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClickEventService {
    private final ClickEventRepository repository;
    private final ShortLinkRepository shortLinkRepository;

    public void recordClick(ShortLink link, String ipAddress, String referrer) {
        repository.save(ClickEvent.builder()
                .shortLink(link)
                .clickedAt(LocalDateTime.now())
                .ipAddress(ipAddress)
                .referrer(referrer)
                .build());
    }

    public CountByShortLinkIdDto countByShortLinkId(Long id){
        ShortLink shortLink = shortLinkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException("ссылка не найдена"));
        return new CountByShortLinkIdDto(shortLink.getShortCode(), repository.countClickEventByShortLinkId(id));
    }
}
