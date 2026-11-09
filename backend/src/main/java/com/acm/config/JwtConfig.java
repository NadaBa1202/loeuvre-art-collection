package com.acm.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtConfig {

    @Bean
    SecretKey jwtSecretKey(SecurityProperties props) {
        byte[] bytes = Base64.getDecoder().decode(props.jwt().secret());
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 bytes (base64 encoded).");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey key, SecurityProperties props) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)   // any other algorithm (including "none") is rejected
                .build();

        OAuth2TokenValidator<Jwt> issuerAndExpiry = JwtValidators.createDefaultWithIssuer(props.jwt().issuer());
        OAuth2TokenValidator<Jwt> audience = new JwtClaimValidator<List<String>>(
                JwtClaimNames.AUD, aud -> aud != null && aud.contains(props.jwt().audience()));

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(issuerAndExpiry, audience));
        return decoder;
    }
}