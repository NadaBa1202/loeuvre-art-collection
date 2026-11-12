package com.acm.dto;

import jakarta.validation.constraints.*;

public record ArtisteRequest(
        @NotBlank(message = "Le nom est obligatoire.")
        @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères.")
        String nom,

        @Size(max = 100, message = "La nationalité ne doit pas dépasser 100 caractères.")
        String nationalite,

        @Min(value = -3000, message = "L'année de naissance doit être comprise entre -3000 et 2100.")
        @Max(value = 2100, message = "L'année de naissance doit être comprise entre -3000 et 2100.")
        Integer anneeNaissance,

        @Min(value = -3000, message = "L'année de décès doit être comprise entre -3000 et 2100.")
        @Max(value = 2100, message = "L'année de décès doit être comprise entre -3000 et 2100.")
        Integer anneeDeces,

        @Size(max = 150, message = "Le mouvement ne doit pas dépasser 150 caractères.")
        String mouvement,

        @Size(max = 5000, message = "La biographie ne doit pas dépasser 5000 caractères.")
        String biographie,

        @Size(max = 500, message = "L'URL de la photo ne doit pas dépasser 500 caractères.")
        @Pattern(regexp = "^https://\\S+$", message = "L'URL de la photo doit commencer par https://")
        String photoUrl) {
}