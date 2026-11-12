package com.acm.service;

import com.acm.dto.ArtisteRequest;
import com.acm.dto.ArtisteResponse;
import com.acm.entity.Artiste;
import com.acm.exception.ApiException;
import com.acm.repository.ArtisteRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtisteService {

    private final ArtisteRepository artistes;

    public ArtisteService(ArtisteRepository artistes) {
        this.artistes = artistes;
    }

    @Transactional(readOnly = true)
    public List<ArtisteResponse> list() {
        return artistes.findAll(Sort.by(Sort.Order.asc("nom").ignoreCase()))
                .stream().map(ArtisteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ArtisteResponse get(Long id) {
        return ArtisteResponse.from(find(id));
    }

    @Transactional
    public ArtisteResponse create(ArtisteRequest req) {
        checkYears(req);
        Artiste a = new Artiste(req.nom().trim(), clean(req.nationalite()), req.anneeNaissance(),
                req.anneeDeces(), clean(req.mouvement()), clean(req.biographie()), clean(req.photoUrl()));
        return ArtisteResponse.from(artistes.save(a));
    }

    @Transactional
    public ArtisteResponse update(Long id, ArtisteRequest req) {
        checkYears(req);
        Artiste a = find(id);
        a.update(req.nom().trim(), clean(req.nationalite()), req.anneeNaissance(), req.anneeDeces(),
                clean(req.mouvement()), clean(req.biographie()), clean(req.photoUrl()));
        return ArtisteResponse.from(a);
    }

    @Transactional
    public void delete(Long id) {
        if (!artistes.existsById(id)) {
            throw notFound();
        }
        artistes.deleteById(id);   // artworks will be removed with it once Oeuvre exists
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
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Données invalides.", java.util.List.of("L'année de décès ne peut pas précéder l'année de naissance."));
        }
    }

    private static String clean(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}