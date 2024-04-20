package com.sa.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "social_accounts")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String provider;

    @Column(length = 50, name = "provider_id", nullable = false)
    private String providerId;

    @Column(length = 150, nullable = false)
    private String email;

    @Column(name = "social_name", nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
