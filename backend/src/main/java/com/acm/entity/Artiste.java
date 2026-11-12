package com.acm.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "artistes")
public class Artiste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 100)
    private String nationalite;

    private Integer anneeNaissance;

    private Integer anneeDeces;          

    @Column(length = 150)
    private String mouvement;

    @Column(length = 5000)
    private String biographie;

    @Column(length = 500)
    private String photoUrl;             

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Artiste() {}

    public Artiste(String nom, String nationalite, Integer anneeNaissance, Integer anneeDeces,
                   String mouvement, String biographie, String photoUrl) {
        update(nom, nationalite, anneeNaissance, anneeDeces, mouvement, biographie, photoUrl);
    }

    public void update(String nom, String nationalite, Integer anneeNaissance, Integer anneeDeces,
                       String mouvement, String biographie, String photoUrl) {
        this.nom = nom;
        this.nationalite = nationalite;
        this.anneeNaissance = anneeNaissance;
        this.anneeDeces = anneeDeces;
        this.mouvement = mouvement;
        this.biographie = biographie;
        this.photoUrl = photoUrl;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getNationalite() { return nationalite; }
    public Integer getAnneeNaissance() { return anneeNaissance; }
    public Integer getAnneeDeces() { return anneeDeces; }
    public String getMouvement() { return mouvement; }
    public String getBiographie() { return biographie; }
    public String getPhotoUrl() { return photoUrl; }
}