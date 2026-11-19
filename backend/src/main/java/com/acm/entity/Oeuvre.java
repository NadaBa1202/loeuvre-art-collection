package com.acm.entity;

import jakarta.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "oeuvres", indexes = @Index(columnList = "artiste_id"))
public class Oeuvre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    private Integer anneeCreation;

    @Column(length = 150)
    private String technique;

    @Column(length = 5000)
    private String description;

    @Column(length = 500)
    private String imageUrl;            // URL only, never a file

    @Column(length = 500)
    private String credit;              // image source and licence

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artiste_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)   // deleting the artist deletes the works (FK ... ON DELETE CASCADE)
    private Artiste artiste;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Oeuvre() {}

    public Oeuvre(String titre, Integer anneeCreation, String technique, String description,
                  String imageUrl, String credit, Artiste artiste) {
        update(titre, anneeCreation, technique, description, imageUrl, credit, artiste);
    }

    public void update(String titre, Integer anneeCreation, String technique, String description,
                       String imageUrl, String credit, Artiste artiste) {
        this.titre = titre;
        this.anneeCreation = anneeCreation;
        this.technique = technique;
        this.description = description;
        this.imageUrl = imageUrl;
        this.credit = credit;
        this.artiste = artiste;
    }

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public Integer getAnneeCreation() { return anneeCreation; }
    public String getTechnique() { return technique; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getCredit() { return credit; }
    public Artiste getArtiste() { return artiste; }
}