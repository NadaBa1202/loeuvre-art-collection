package com.acm.service;

import com.acm.dto.AuthResponse;

public record LoginResult(AuthResponse body, String refreshToken) {}