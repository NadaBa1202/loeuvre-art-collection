package com.acm.dto;

import com.acm.entity.Artiste;

public record ArtisteResponse(Long id, String nom, String nationalite, Integer anneeNaissance,
                              Integer anneeDeces, String mouvement, String biographie, String photoUrl) {
    public static ArtisteResponse from(Artiste a) {
        return new ArtisteResponse(a.getId(), a.getNom(), a.getNationalite(), a.getAnneeNaissance(),
                a.getAnneeDeces(), a.getMouvement(), a.getBiographie(), a.getPhotoUrl());
    }
}