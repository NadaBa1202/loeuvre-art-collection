package com.acm.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(@Valid Jwt jwt, @Min(1) int refreshTokenDays, boolean cookieSecure) {

    public record Jwt(
            @NotBlank @Size(min = 43) String secret,
            @NotBlank String issuer,
            @NotBlank String audience,
            @Min(1) @Max(60) int accessTokenMinutes) {
    }
}