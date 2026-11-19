package com.acm.dto;

import com.acm.entity.Oeuvre;

public record OeuvreResponse(Long id, String titre, Integer anneeCreation, String technique,
                             String description, String imageUrl, String credit,
                             Long artisteId, String artisteNom) {
    public static OeuvreResponse from(Oeuvre o) {
        return new OeuvreResponse(o.getId(), o.getTitre(), o.getAnneeCreation(), o.getTechnique(),
                o.getDescription(), o.getImageUrl(), o.getCredit(),
                o.getArtiste().getId(), o.getArtiste().getNom());
    }
}