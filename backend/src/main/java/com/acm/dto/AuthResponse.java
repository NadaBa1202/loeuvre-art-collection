package com.acm.dto;

public record AuthResponse(String accessToken, long expiresIn, UserResponse user) {}