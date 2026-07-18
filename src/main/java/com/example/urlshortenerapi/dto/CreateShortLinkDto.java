package com.example.urlshortenerapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter @Setter
@NoArgsConstructor
public class CreateShortLinkDto {
    @NotBlank(message = "url не может быть пустым") @URL
    private String url;
}
