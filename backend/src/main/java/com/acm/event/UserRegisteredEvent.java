package com.acm.event;

public record UserRegisteredEvent(String prenom, String nom, String email) {}