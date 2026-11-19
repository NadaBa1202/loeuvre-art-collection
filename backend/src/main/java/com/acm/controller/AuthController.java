package com.acm.controller;

import com.acm.config.SecurityProperties;
import com.acm.dto.*;
import com.acm.exception.ApiException;
import com.acm.security.AttemptLimiter;
import com.acm.service.AuthService;
import com.acm.service.LoginResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.Locale;
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

    // F19: 5 failures / 15 min per (IP + e-mail), 30 failures / 15 min per IP, 10 sign-ups / hour per IP
    private final AttemptLimiter loginByAccount = new AttemptLimiter(5, Duration.ofMinutes(15));
    private final AttemptLimiter loginByIp = new AttemptLimiter(30, Duration.ofMinutes(15));
    private final AttemptLimiter registerByIp = new AttemptLimiter(10, Duration.ofHours(1));

    public AuthController(AuthService auth, SecurityProperties props) {
        this.auth = auth;
        this.props = props;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest req, HttpServletRequest http) {
        String ip = http.getRemoteAddr();
        if (registerByIp.isBlocked(ip)) throw tooMany();
        registerByIp.record(ip);
        return auth.register(req);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req, HttpServletRequest http) {
        String ip = http.getRemoteAddr();
        String key = ip + "|" + req.email().trim().toLowerCase(Locale.ROOT);
        if (loginByAccount.isBlocked(key) || loginByIp.isBlocked(ip)) throw tooMany();
        try {
            LoginResult result = auth.login(req);
            loginByAccount.reset(key);
            return withRefreshCookie(result);
        } catch (ApiException e) {
            if (e.getStatus() == HttpStatus.UNAUTHORIZED) {
                loginByAccount.record(key);
                loginByIp.record(ip);
            }
            throw e;
        }
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

    private static ApiException tooMany() {
        return new ApiException(HttpStatus.TOO_MANY_REQUESTS, "Trop de tentatives. Réessayez plus tard.");
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