package com.acm.dto;

import com.acm.entity.Role;
import com.acm.entity.User;
import java.util.Set;

public record UserResponse(Long id, String prenom, String nom, String email, Set<Role> roles) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getPrenom(), u.getNom(), u.getEmail(), Set.copyOf(u.getRoles()));
    }
}