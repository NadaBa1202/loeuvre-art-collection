package com.acm.controller;

import com.acm.dto.OeuvreRequest;
import com.acm.dto.OeuvreResponse;
import com.acm.service.OeuvreService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/oeuvres")
public class OeuvreController {

    private final OeuvreService oeuvres;

    public OeuvreController(OeuvreService oeuvres) {
        this.oeuvres = oeuvres;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATEUR')")
    public List<OeuvreResponse> list(@RequestParam(required = false) Long artisteId) {
        return oeuvres.list(artisteId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATEUR')")
    public OeuvreResponse get(@PathVariable Long id) {
        return oeuvres.get(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATEUR')")
    public ResponseEntity<OeuvreResponse> create(@Valid @RequestBody OeuvreRequest req) {
        OeuvreResponse created = oeuvres.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OeuvreResponse update(@PathVariable Long id, @Valid @RequestBody OeuvreRequest req) {
        return oeuvres.update(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        oeuvres.delete(id);
    }
}