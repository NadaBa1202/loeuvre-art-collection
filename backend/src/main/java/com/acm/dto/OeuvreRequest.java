package com.acm.dto;

import jakarta.validation.constraints.*;

public record OeuvreRequest(
        @NotBlank(message = "Le titre est obligatoire.")
        @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères.")
        String titre,

        @NotNull(message = "L'artiste est obligatoire.")
        Long idArtiste,

        @Min(value = -3000, message = "L'année de création doit être comprise entre -3000 et 2100.")
        @Max(value = 2100, message = "L'année de création doit être comprise entre -3000 et 2100.")
        Integer anneeCreation,

        @Size(max = 150, message = "La technique ne doit pas dépasser 150 caractères.")
        String technique,

        @Size(max = 5000, message = "La description ne doit pas dépasser 5000 caractères.")
        String description,

        @Size(max = 500, message = "L'URL de l'image ne doit pas dépasser 500 caractères.")
        @Pattern(regexp = "^https://\\S+$", message = "L'URL de l'image doit commencer par https://")
        String imageUrl,

        @Size(max = 500, message = "Le crédit ne doit pas dépasser 500 caractères.")
        String credit) {
}