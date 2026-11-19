package com.acm.service;

import com.acm.dto.OeuvreRequest;
import com.acm.dto.OeuvreResponse;
import com.acm.entity.Artiste;
import com.acm.entity.Oeuvre;
import com.acm.exception.ApiException;
import com.acm.repository.ArtisteRepository;
import com.acm.repository.OeuvreRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OeuvreService {

    private static final Sort BY_TITLE = Sort.by(Sort.Order.asc("titre").ignoreCase());

    private final OeuvreRepository oeuvres;
    private final ArtisteRepository artistes;

    public OeuvreService(OeuvreRepository oeuvres, ArtisteRepository artistes) {
        this.oeuvres = oeuvres;
        this.artistes = artistes;
    }

    @Transactional(readOnly = true)
    public List<OeuvreResponse> list(Long artisteId) {
        List<Oeuvre> found = artisteId == null
                ? oeuvres.findAll(BY_TITLE)
                : oeuvres.findByArtisteId(artisteId, BY_TITLE);
        return found.stream().map(OeuvreResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public OeuvreResponse get(Long id) {
        return OeuvreResponse.from(find(id));
    }

    @Transactional
    public OeuvreResponse create(OeuvreRequest req) {
        Artiste artiste = findArtiste(req.idArtiste());
        Oeuvre o = new Oeuvre(req.titre().trim(), req.anneeCreation(), clean(req.technique()),
                clean(req.description()), clean(req.imageUrl()), clean(req.credit()), artiste);
        return OeuvreResponse.from(oeuvres.save(o));
    }

    @Transactional
    public OeuvreResponse update(Long id, OeuvreRequest req) {
        Oeuvre o = find(id);
        Artiste artiste = findArtiste(req.idArtiste());   // an artwork can be moved to another artist
        o.update(req.titre().trim(), req.anneeCreation(), clean(req.technique()),
                clean(req.description()), clean(req.imageUrl()), clean(req.credit()), artiste);
        return OeuvreResponse.from(o);
    }

    @Transactional
    public void delete(Long id) {
        if (!oeuvres.existsById(id)) {
            throw notFound();
        }
        oeuvres.deleteById(id);
    }

    private Oeuvre find(Long id) {
        return oeuvres.findById(id).orElseThrow(OeuvreService::notFound);
    }

    private Artiste findArtiste(Long id) {
        return artistes.findById(id).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                "Données invalides.", List.of("L'artiste sélectionné n'existe pas.")));
    }

    private static ApiException notFound() {
        return new ApiException(HttpStatus.NOT_FOUND, "Œuvre introuvable.");
    }

    private static String clean(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}