package com.example.urlshortenerapi;

import com.example.urlshortenerapi.dto.CreateShortLinkDto;
import com.example.urlshortenerapi.entity.User;
import com.example.urlshortenerapi.repository.ShortLinkRepository;
import com.example.urlshortenerapi.repository.UserRepository;
import com.example.urlshortenerapi.service.ClickEventService;
import com.example.urlshortenerapi.service.ShortLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortLinkServiceTest {

    @Mock
    private ShortLinkRepository repository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ShortLinkService shortLinkService;

    @BeforeEach
    void setUp(){
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);

        ReflectionTestUtils.setField(shortLinkService, "startLink", "http://localhost:8080/");
    }

    @Test
    void shouldCreateLinkSuccessfully(){
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(repository.existsShortLinkByShortCode(anyString())).thenReturn(false);

        CreateShortLinkDto dto = new CreateShortLinkDto();
        dto.setUrl("https://example.com");

        String shortLink = shortLinkService.createLink(dto);

        assertTrue(shortLink.startsWith("http://localhost:8080/"));
        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldRetryWhenFirstCodeIsTaken(){
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(repository.existsShortLinkByShortCode(anyString())).thenReturn(true, false);

        CreateShortLinkDto dto = new CreateShortLinkDto();
        dto.setUrl("https://example.com");

        String shortLink = shortLinkService.createLink(dto);

        assertTrue(shortLink.startsWith("http://localhost:8080/"));
        verify(repository, times(2)).existsShortLinkByShortCode(anyString());
        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldThrowWhenAllAttemptsExhausted(){
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(repository.existsShortLinkByShortCode(anyString())).thenReturn(true);

        CreateShortLinkDto dto = new CreateShortLinkDto();
        dto.setUrl("https://example.com");

        assertThrows(RuntimeException.class, () -> shortLinkService.createLink(dto));
        verify(repository, never()).save(any());
    }

}
