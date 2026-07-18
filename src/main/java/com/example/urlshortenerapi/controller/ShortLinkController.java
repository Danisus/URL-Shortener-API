package com.example.urlshortenerapi.controller;

import com.example.urlshortenerapi.dto.CreateShortLinkDto;
import com.example.urlshortenerapi.dto.ShortLinkResponse;
import com.example.urlshortenerapi.entity.ShortLink;
import com.example.urlshortenerapi.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ShortLinkController {
    private final ShortLinkService service;

    @PostMapping("links")
    public ResponseEntity<String> createShortLink(@Valid @RequestBody CreateShortLinkDto createShortLinkDto){
        return ResponseEntity.status(201).body(service.createLink(createShortLinkDto));
    }

    @GetMapping("links")
    public List<ShortLinkResponse> findAll(){
        return service.findAll();
    }

    @GetMapping("links/{id}")
    public ShortLinkResponse findById(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> findByCode(@PathVariable String code, HttpServletRequest request){
        ShortLink link = service.findByShortCode(code, request.getRemoteAddr(), request.getHeader("Referer"));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(link.getOriginalLink()));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }
}
