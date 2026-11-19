package com.example.urlshortenerapi;

import com.example.urlshortenerapi.dto.CountByShortLinkIdDto;
import com.example.urlshortenerapi.entity.ShortLink;
import com.example.urlshortenerapi.exception.LinkNotFoundException;
import com.example.urlshortenerapi.repository.ClickEventRepository;
import com.example.urlshortenerapi.repository.ShortLinkRepository;
import com.example.urlshortenerapi.service.ClickEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClickEventServiceTest {
    @Mock
    private ClickEventRepository repository;
    @Mock
    private ShortLinkRepository shortLinkRepository;
    @InjectMocks
    private ClickEventService service;

    @Test
    void countByShortLinkIdSuccess(){
        ShortLink shortLink = new ShortLink();
        shortLink.setId(2L);
        shortLink.setShortCode("abc123");
        when(shortLinkRepository.findById(2L)).thenReturn(Optional.of(shortLink));
        when(repository.countClickEventByShortLinkId(2L)).thenReturn(5L);

        CountByShortLinkIdDto dto = service.countByShortLinkId(2L);

        assertEquals("abc123", dto.shortCode());
        assertEquals(5, dto.count());
    }

    @Test
    void countByShortLinkIdThrowsWhenLinkNotFound(){
        when(shortLinkRepository.findById(67L)).thenReturn(Optional.empty());

        assertThrows(LinkNotFoundException.class, () -> service.countByShortLinkId(67L));
    }
}
