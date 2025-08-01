package com.labo.reservationsystem.repository;

import com.labo.reservationsystem.entity.Reservation;
import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUtilisateur(Utilisateur utilisateur);
    List<Reservation> findByEquipement(Equipement equipement);
}