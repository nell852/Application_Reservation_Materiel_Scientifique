package com.labo.reservationsystem.service;

import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.entity.Reservation;
import com.labo.reservationsystem.repository.EquipementRepository;
import com.labo.reservationsystem.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final EquipementRepository equipementRepository;
    private final ReservationRepository reservationRepository;

    public DashboardService(EquipementRepository equipementRepository, ReservationRepository reservationRepository) {
        this.equipementRepository = equipementRepository;
        this.reservationRepository = reservationRepository;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Nombre total d'équipements
        long totalEquipements = equipementRepository.count();
        stats.put("totalEquipements", totalEquipements);

        // Répartition par état
        Map<Equipement.Etat, Long> etatCounts = new HashMap<>();
        for (Equipement.Etat etat : Equipement.Etat.values()) {
            long count = equipementRepository.findAll().stream()
                    .filter(e -> e.getEtat() == etat)
                    .count();
            etatCounts.put(etat, count);
        }
        stats.put("etatCounts", etatCounts);

        // Nombre de réservations actives
        long reservationsActives = reservationRepository.findAll().stream()
                .filter(r -> r.getStatut() == Reservation.Statut.CONFIRMEE || r.getStatut() == Reservation.Statut.EN_ATTENTE)
                .count();
        stats.put("reservationsActives", reservationsActives);

        return stats;
    }
}