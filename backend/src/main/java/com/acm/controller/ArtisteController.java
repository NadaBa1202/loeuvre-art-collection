package com.acm.controller;

import com.acm.dto.ArtisteRequest;
import com.acm.dto.ArtisteResponse;
import com.acm.service.ArtisteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/artistes")
public class ArtisteController {

    private final ArtisteService artistes;

    public ArtisteController(ArtisteService artistes) {
        this.artistes = artistes;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATEUR')")
    public List<ArtisteResponse> list() {
        return artistes.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATEUR')")
    public ArtisteResponse get(@PathVariable Long id) {
        return artistes.get(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','COLLABORATEUR')")
    public ResponseEntity<ArtisteResponse> create(@Valid @RequestBody ArtisteRequest req) {
        ArtisteResponse created = artistes.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ArtisteResponse update(@PathVariable Long id, @Valid @RequestBody ArtisteRequest req) {
        return artistes.update(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        artistes.delete(id);
    }
}