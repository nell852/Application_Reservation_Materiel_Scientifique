package com.labo.reservationsystem.service;

import com.labo.reservationsystem.entity.Reservation;
import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ReservationService {

    private static final Logger LOGGER = Logger.getLogger(ReservationService.class.getName());

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public List<Reservation> findByUtilisateur(Utilisateur utilisateur) {
        return reservationRepository.findByUtilisateur(utilisateur);
    }

    public List<Reservation> findByEquipement(Equipement equipement) {
        return reservationRepository.findByEquipement(equipement);
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        LOGGER.info("Tentative d'enregistrement de la réservation : " + reservation);
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null.");
        }
        if (reservation.getEquipement() == null) {
            throw new IllegalArgumentException("L'équipement ne peut pas être null.");
        }
        if (reservation.getUtilisateur() == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null.");
        }
        if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de début et de fin ne peuvent pas être null.");
        }
        if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }
        // Commenter temporairement la vérification des conflits pour tester
        /*
        List<Reservation> existingReservations = reservationRepository.findByEquipement(reservation.getEquipement());
        for (Reservation existing : existingReservations) {
            if (existing.getStatut() != Reservation.Statut.ANNULEE &&
                reservation.getDateDebut().isBefore(existing.getDateFin()) &&
                reservation.getDateFin().isAfter(existing.getDateDebut())) {
                throw new IllegalArgumentException("Conflit de réservation : l'équipement est déjà réservé pour ce créneau.");
            }
        }
        */
        Reservation savedReservation = reservationRepository.save(reservation);
        LOGGER.info("Réservation enregistrée avec succès : " + savedReservation);
        return savedReservation;
    }

    @Transactional
    public Reservation testSave(Reservation reservation) {
        LOGGER.info("Test d'enregistrement simplifié : " + reservation);
        Reservation savedReservation = reservationRepository.save(reservation);
        LOGGER.info("Test d'enregistrement réussi : " + savedReservation);
        return savedReservation;
    }

    @Transactional
    public void cancelReservation(Long id) {
        LOGGER.info("Annulation de la réservation ID : " + id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée."));
        reservation.setStatut(Reservation.Statut.ANNULEE);
        reservationRepository.save(reservation);
        LOGGER.info("Réservation annulée avec succès : " + reservation);
    }

	public Optional<Reservation> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}