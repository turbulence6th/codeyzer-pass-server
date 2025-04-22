package com.codeyzer.pass.sunucu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_kimlik", referencedColumnName = "kullanici_kimlik", nullable = false)
    private Kullanici kullanici;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}
