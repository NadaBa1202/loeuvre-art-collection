package com.acm.service;

import com.acm.dto.*;
import com.acm.entity.RefreshToken;
import com.acm.entity.Role;
import com.acm.entity.User;
import com.acm.exception.ApiException;
import com.acm.repository.RefreshTokenRepository;
import com.acm.repository.UserRepository;
import com.acm.security.PasswordPolicy;
import com.acm.security.TokenService;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder encoder;
    private final PasswordPolicy policy;
    private final TokenService tokens;
    private final String dummyHash;   // used to keep login timing similar for unknown e-mails

    public AuthService(UserRepository users, RefreshTokenRepository refreshTokens,
                       PasswordEncoder encoder, PasswordPolicy policy, TokenService tokens) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.encoder = encoder;
        this.policy = policy;
        this.tokens = tokens;
        this.dummyHash = encoder.encode("dummy-password-used-for-timing");
    }

    @Transactional
    public UserResponse register(RegisterRequest req) {
        String email = normalize(req.email());

        List<String> problems = policy.check(req.password(), email, req.prenom(), req.nom());
        if (!problems.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Mot de passe trop faible.", problems);
        }
        if (users.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Cette adresse e-mail est déjà utilisée.");
        }

        User user = new User(email, encoder.encode(req.password()),
                req.prenom().trim(), req.nom().trim(), req.tel().trim());
        user.addRole(Role.COLLABORATEUR);   // every new account is a COLLABORATEUR, never chosen by the client
        try {
            users.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {   // two simultaneous registrations
            throw new ApiException(HttpStatus.CONFLICT, "Cette adresse e-mail est déjà utilisée.");
        }
        return UserResponse.from(user);
    }

    @Transactional
    public LoginResult login(LoginRequest req) {
        User user = users.findByEmail(normalize(req.email())).orElse(null);
        String hash = user != null ? user.getPassword() : dummyHash;

        boolean ok = !PasswordPolicy.tooLongForBcrypt(req.password())
                && encoder.matches(req.password(), hash)
                && user != null;
        if (!ok) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Identifiants invalides.");   // same message in every case
        }
        return buildResult(user, UUID.randomUUID().toString());
    }

    @Transactional(noRollbackFor = ApiException.class)
    public LoginResult refresh(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw sessionExpired();
        }
        RefreshToken stored = refreshTokens.findByTokenHash(tokens.hash(rawToken))
                .orElseThrow(this::sessionExpired);

        if (stored.isRevoked()) {
            // A used token came back: possible theft. Kill the whole session.
            refreshTokens.revokeFamily(stored.getFamilyId());
            throw sessionExpired();
        }
        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw sessionExpired();
        }

        stored.setRevoked(true);   // rotation: this token can never be used again
        return buildResult(stored.getUser(), stored.getFamilyId());
    }

    @Transactional
    public void logout(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return;
        refreshTokens.findByTokenHash(tokens.hash(rawToken))
                .ifPresent(t -> refreshTokens.revokeFamily(t.getFamilyId()));
    }

    @Transactional(readOnly = true)
    public UserResponse me(Long userId) {
        return users.findById(userId).map(UserResponse::from).orElseThrow(this::sessionExpired);
    }

    private LoginResult buildResult(User user, String familyId) {
        String raw = tokens.newRefreshTokenValue();
        refreshTokens.save(new RefreshToken(tokens.hash(raw), user, familyId,
                Instant.now().plus(tokens.refreshTokenLifetime())));
        AuthResponse body = new AuthResponse(tokens.createAccessToken(user),
                tokens.accessTokenSeconds(), UserResponse.from(user));
        return new LoginResult(body, raw);
    }

    private ApiException sessionExpired() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "Session expirée. Veuillez vous reconnecter.");
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}