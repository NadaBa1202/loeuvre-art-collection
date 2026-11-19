package com.acm.config;

import com.acm.entity.User;
import com.acm.repository.UserRepository;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, UserRepository users) throws Exception {
        http
            // Authorization header only (never attached automatically by a browser) -> classic CSRF does not apply.
            // The two cookie endpoints are protected separately (SameSite=Strict, restricted path, custom header).
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST,
                        "/api/auth/register", "/api/auth/login",
                        "/api/auth/refresh", "/api/auth/logout").permitAll()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/dashboard").hasAnyRole("ADMIN", "COLLABORATEUR")
                .requestMatchers(HttpMethod.GET,  "/api/artistes/**", "/api/oeuvres/**").hasAnyRole("ADMIN", "COLLABORATEUR")
                .requestMatchers(HttpMethod.POST, "/api/artistes/**", "/api/oeuvres/**").hasAnyRole("ADMIN", "COLLABORATEUR")
                .requestMatchers(HttpMethod.PUT,    "/api/artistes/**", "/api/oeuvres/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/artistes/**", "/api/oeuvres/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll())
            .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(converter(users))));
        return http.build();
    }

    /**
     * Roles are read from the database on every request, not from the token:
     * - a role change by an admin applies immediately;
     * - a deleted user is rejected (401) even if his access token has not expired yet.
     */
    private Converter<Jwt, AbstractAuthenticationToken> converter(UserRepository users) {
        return jwt -> {
            Long id;
            try {
                id = Long.valueOf(jwt.getSubject());
            } catch (NumberFormatException e) {
                throw new BadCredentialsException("Jeton invalide.");
            }
            User user = users.findById(id).orElseThrow(() -> new BadCredentialsException("Compte inexistant."));
            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .<GrantedAuthority>map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                    .toList();
            return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
        };
    }
}