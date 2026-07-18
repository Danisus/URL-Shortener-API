package com.example.urlshortenerapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "click_events")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "short_link_id", nullable = false)
    private ShortLink shortLink;
    @Column(nullable = false)
    private LocalDateTime clickedAt;
    @Column(nullable = false)
    private String ipAddress;
    private String referrer;
}
