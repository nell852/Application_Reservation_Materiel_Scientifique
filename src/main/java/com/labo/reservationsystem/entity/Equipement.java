package com.labo.reservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "equipements")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le type est requis")
    private String type;

    @NotBlank(message = "Le numéro de série est requis")
    @Column(unique = true)
    private String numeroSerie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Etat etat;

    @NotBlank(message = "Le laboratoire est requis")
    private String laboratoire;

    public enum Etat {
        DISPONIBLE, EN_UTILISATION, EN_MAINTENANCE, HORS_SERVICE
    }

    // Constructeurs
    public Equipement() {}

    public Equipement(String type, String numeroSerie, Etat etat, String laboratoire) {
        this.type = type;
        this.numeroSerie = numeroSerie;
        this.etat = etat;
        this.laboratoire = laboratoire;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public String getLaboratoire() {
        return laboratoire;
    }

    public void setLaboratoire(String laboratoire) {
        this.laboratoire = laboratoire;
    }
}