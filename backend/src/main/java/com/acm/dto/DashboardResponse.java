package com.acm.dto;

import java.util.List;

public record DashboardResponse(long nbArtistes, long nbOeuvres, Long nbUtilisateurs,
        List<TechniqueCount> parTechnique, List<RecentOeuvre> recentes) {
public record TechniqueCount(String technique, long total) {}
public record RecentOeuvre(Long id, String titre, String artisteNom, String technique,
       Integer anneeCreation, String imageUrl) {}
}