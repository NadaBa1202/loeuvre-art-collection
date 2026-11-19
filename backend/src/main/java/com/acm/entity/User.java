package com.acm.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;            // BCrypt hash only

    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false, length = 20)
    private String tel;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected User() {}

    public User(String email, String passwordHash, String prenom, String nom, String tel) {
        this.email = email;
        this.password = passwordHash;
        this.prenom = prenom;
        this.nom = nom;
        this.tel = tel;
    }

    public void addRole(Role role) { roles.add(role); }
    public void setRoles(Set<Role> newRoles) {
        roles.clear();
        roles.addAll(newRoles);
    }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPrenom() { return prenom; }
    public String getNom() { return nom; }
    public String getTel() { return tel; }
    public Set<Role> getRoles() { return roles; }
}