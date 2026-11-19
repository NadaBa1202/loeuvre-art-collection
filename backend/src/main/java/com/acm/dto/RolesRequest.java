package com.acm.dto;

import com.acm.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record RolesRequest(
        @NotEmpty(message = "Au moins un rôle est obligatoire.")
        Set<@NotNull(message = "Rôle invalide.") Role> roles) {
}