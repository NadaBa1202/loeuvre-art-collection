package com.acm.security;

import com.acm.config.SecurityProperties;
import com.acm.entity.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final SecurityProperties props;
    private final SecureRandom random = new SecureRandom();

    public TokenService(JwtEncoder encoder, SecurityProperties props) {
        this.encoder = encoder;
        this.props = props;
    }

    public long accessTokenSeconds() {
        return Duration.ofMinutes(props.jwt().accessTokenMinutes()).toSeconds();
    }

    /** Claims: subject = user id and roles only. No password, no e-mail, no personal data. */
    public String createAccessToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuer(props.jwt().issuer())
                .audience(List.of(props.jwt().audience()))
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenSeconds()))
                .claim("roles", user.getRoles().stream().map(Enum::name).sorted().toList())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String newRefreshTokenValue() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String hash(String rawToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public Duration refreshTokenLifetime() {
        return Duration.ofDays(props.refreshTokenDays());
    }
}