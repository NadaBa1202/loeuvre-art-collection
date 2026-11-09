package com.acm.controller;

import com.acm.config.SecurityProperties;
import com.acm.dto.*;
import com.acm.service.AuthService;
import com.acm.service.LoginResult;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String COOKIE = "acm_refresh";

    private final AuthService auth;
    private final SecurityProperties props;

    public AuthController(AuthService auth, SecurityProperties props) {
        this.auth = auth;
        this.props = props;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest req) {
        return auth.register(req);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return withRefreshCookie(auth.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = COOKIE, required = false) String refreshToken,
            @RequestHeader(name = "X-Requested-With") String requestedWith) {
        return withRefreshCookie(auth.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = COOKIE, required = false) String refreshToken,
            @RequestHeader(name = "X-Requested-With") String requestedWith) {
        auth.logout(refreshToken);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie("", Duration.ZERO).toString())
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return auth.me(Long.valueOf(jwt.getSubject()));
    }

    private ResponseEntity<AuthResponse> withRefreshCookie(LoginResult result) {
        ResponseCookie cookie = cookie(result.refreshToken(), Duration.ofDays(props.refreshTokenDays()));
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.body());
    }

    private ResponseCookie cookie(String value, Duration maxAge) {
        return ResponseCookie.from(COOKIE, value)
                .httpOnly(true)
                .secure(props.cookieSecure())
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(maxAge)
                .build();
    }
}