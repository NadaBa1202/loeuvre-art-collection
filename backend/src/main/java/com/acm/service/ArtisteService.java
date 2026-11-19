package com.acm.service;

import com.acm.dto.ArtisteRequest;
import com.acm.dto.ArtisteResponse;
import com.acm.entity.Artiste;
import com.acm.exception.ApiException;
import com.acm.repository.ArtisteRepository;
import com.acm.repository.OeuvreRepository;
import com.acm.repository.OeuvreRepository.ArtisteCount;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtisteService {

    private final ArtisteRepository artistes;
    private final OeuvreRepository oeuvres;

    public ArtisteService(ArtisteRepository artistes, OeuvreRepository oeuvres) {
        this.artistes = artistes;
        this.oeuvres = oeuvres;
    }

    @Transactional(readOnly = true)
    public List<ArtisteResponse> list() {
        Map<Long, Long> counts = oeuvres.countByArtiste().stream()
                .collect(Collectors.toMap(ArtisteCount::getArtisteId, ArtisteCount::getTotal));
        return artistes.findAll(Sort.by(Sort.Order.asc("nom").ignoreCase())).stream()
                .map(a -> ArtisteResponse.from(a, counts.getOrDefault(a.getId(), 0L)))
                .toList();
    }

    @Transactional(readOnly = true)
    public ArtisteResponse get(Long id) {
        return ArtisteResponse.from(find(id), oeuvres.countByArtisteId(id));
    }

    @Transactional
    public ArtisteResponse create(ArtisteRequest req) {
        checkYears(req);
        Artiste a = new Artiste(req.nom().trim(), clean(req.nationalite()), req.anneeNaissance(),
                req.anneeDeces(), clean(req.mouvement()), clean(req.biographie()), clean(req.photoUrl()));
        return ArtisteResponse.from(artistes.save(a), 0L);
    }

    @Transactional
    public ArtisteResponse update(Long id, ArtisteRequest req) {
        checkYears(req);
        Artiste a = find(id);
        a.update(req.nom().trim(), clean(req.nationalite()), req.anneeNaissance(), req.anneeDeces(),
                clean(req.mouvement()), clean(req.biographie()), clean(req.photoUrl()));
        return ArtisteResponse.from(a, oeuvres.countByArtisteId(id));
    }

    /** Deleting the artist also deletes their artworks: the foreign key of "oeuvres" is ON DELETE CASCADE. */
    @Transactional
    public void delete(Long id) {
    	if (!artistes.existsById(id)) throw notFound();
    	oeuvres.deleteByArtisteId(id);
    	artistes.deleteById(id);
    }

    private Artiste find(Long id) {
        return artistes.findById(id).orElseThrow(ArtisteService::notFound);
    }

    private static ApiException notFound() {
        return new ApiException(HttpStatus.NOT_FOUND, "Artiste introuvable.");
    }

    private static void checkYears(ArtisteRequest req) {
        if (req.anneeNaissance() != null && req.anneeDeces() != null
                && req.anneeDeces() < req.anneeNaissance()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Données invalides.",
                    List.of("L'année de décès ne peut pas précéder l'année de naissance."));
        }
    }

    private static String clean(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}