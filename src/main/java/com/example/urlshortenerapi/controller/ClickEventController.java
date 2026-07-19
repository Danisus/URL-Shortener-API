package com.example.urlshortenerapi.controller;

import com.example.urlshortenerapi.dto.CountByShortLinkIdDto;
import com.example.urlshortenerapi.service.ClickEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClickEventController {
    private final ClickEventService clickEventService;

    @GetMapping("/links/{id}/stats")
    public CountByShortLinkIdDto countByShortLinkId(@PathVariable Long id){
        return clickEventService.countByShortLinkId(id);
    }
}
