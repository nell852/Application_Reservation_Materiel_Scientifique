package com.labo.reservationsystem.repository;

import com.labo.reservationsystem.entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    Optional<Equipement> findByNumeroSerie(String numeroSerie);
    
}