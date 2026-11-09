package com.acm.entity;

import jakarta.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "refresh_tokens", indexes = @Index(columnList = "familyId"))
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false, length = 36)
    private String familyId;            // all tokens of one login session share it

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    protected RefreshToken() {}

    public RefreshToken(String tokenHash, User user, String familyId, Instant expiresAt) {
        this.tokenHash = tokenHash;
        this.user = user;
        this.familyId = familyId;
        this.expiresAt = expiresAt;
    }

    public User getUser() { return user; }
    public String getFamilyId() { return familyId; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}