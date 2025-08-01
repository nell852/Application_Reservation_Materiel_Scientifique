package com.labo.reservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenances")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipement_id", nullable = false)
    @NotNull(message = "L'équipement est requis")
    private Equipement equipement;

    @NotNull(message = "La date est requise")
    private LocalDateTime date;

    @NotBlank(message = "Les actions effectuées sont requises")
    private String actionsEffectuees;

    // Constructeurs
    public Maintenance() {}

    public Maintenance(Equipement equipement, LocalDateTime date, String actionsEffectuees) {
        this.equipement = equipement;
        this.date = date;
        this.actionsEffectuees = actionsEffectuees;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getActionsEffectuees() {
        return actionsEffectuees;
    }

    public void setActionsEffectuees(String actionsEffectuees) {
        this.actionsEffectuees = actionsEffectuees;
    }
}