package com.acm.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Size(max = 50) String prenom,
        @NotBlank @Size(max = 50) String nom,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Pattern(regexp = "^\\+?[0-9 ]{8,15}$", message = "Numéro de téléphone invalide") String tel,
        @NotBlank @Size(min = 12, max = 128, message = "Le mot de passe doit contenir entre 12 et 128 caractères") String password) {

    @Override
    public String toString() {
        return "RegisterRequest[email=" + email + "]";
    }
}