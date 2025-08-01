package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Reservation;
import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.service.EquipementService;
import com.labo.reservationsystem.service.ReservationService;
import com.labo.reservationsystem.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private static final Logger LOGGER = Logger.getLogger(ReservationController.class.getName());

    private final ReservationService reservationService;
    private final EquipementService equipementService;
    private final UtilisateurRepository utilisateurRepository;

    public ReservationController(ReservationService reservationService, EquipementService equipementService, UtilisateurRepository utilisateurRepository) {
        this.reservationService = reservationService;
        this.equipementService = equipementService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    public String afficherIndexReservations() {
        LOGGER.info("Affichage de la page d'index des réservations");
        return "reservations/index";
    }

    @GetMapping("/list")
    public String listReservations(Model model, Authentication authentication) {
        LOGGER.info("Affichage de la liste des réservations pour l'utilisateur : " + authentication.getName());
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé : " + authentication.getName()));
            
            boolean isGestionnaire = utilisateur.getRole() == Utilisateur.Role.GESTIONNAIRE;

            if (isGestionnaire) {
                // Gestionnaire : voir toutes les réservations
                List<Reservation> allReservations = reservationService.findAll();
                LOGGER.info("Affichage de toutes les réservations pour le gestionnaire, total : " + allReservations.size());
                model.addAttribute("reservations", allReservations);
            } else {
                // Chercheur : voir uniquement ses réservations
                List<Reservation> userReservations = reservationService.findByUtilisateur(utilisateur);
                LOGGER.info("Affichage des réservations de l'utilisateur : " + utilisateur.getEmail() + ", total : " + userReservations.size());
                model.addAttribute("reservations", userReservations);
            }

            return "reservations/list";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'affichage des réservations : " + e.getMessage());
            model.addAttribute("error", "Erreur lors du chargement des réservations : " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/form")
    public String showReservationForm(Model model) {
        LOGGER.info("Affichage du formulaire de réservation");
        try {
            model.addAttribute("reservation", new Reservation());
            model.addAttribute("equipements", equipementService.findAll());
            model.addAttribute("statuts", Reservation.Statut.values());
            return "reservations/form";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'affichage du formulaire : " + e.getMessage());
            model.addAttribute("error", "Erreur lors du chargement du formulaire : " + e.getMessage());
            return "error";
        }
    }

    @PostMapping
    public String saveReservation(
            @RequestParam("equipementId") Long equipementId,
            @RequestParam("dateDebut") String dateDebut,
            @RequestParam("dateFin") String dateFin,
            @RequestParam("statut") String statut,
            Model model, Authentication authentication) {
        LOGGER.info("Tentative d'enregistrement de la réservation : equipementId=" + equipementId + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", statut=" + statut);
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé : " + authentication.getName()));
            Equipement equipement = equipementService.findById(equipementId)
                    .orElseThrow(() -> new IllegalArgumentException("Équipement non trouvé : " + equipementId));

            Reservation reservation = new Reservation();
            reservation.setUtilisateur(utilisateur);
            reservation.setEquipement(equipement);
            reservation.setDateDebut(LocalDateTime.parse(dateDebut));
            reservation.setDateFin(LocalDateTime.parse(dateFin));
            reservation.setStatut(Reservation.Statut.valueOf(statut));

            reservationService.save(reservation);
            LOGGER.info("Réservation enregistrée avec succès : " + reservation);
            return "redirect:/reservations";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("error", "Erreur lors de l'enregistrement de la réservation : " + e.getMessage());
            model.addAttribute("equipements", equipementService.findAll());
            model.addAttribute("statuts", Reservation.Statut.values());
            return "reservations/form";
        }
    }

    @PostMapping("/test-save")
    public String testSaveReservation(Model model, Authentication authentication) {
        LOGGER.info("Test d'enregistrement simplifié");
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé : " + authentication.getName()));
            Reservation reservation = new Reservation();
            reservation.setUtilisateur(utilisateur);
            reservation.setEquipement(equipementService.findAll().get(0));
            reservation.setDateDebut(LocalDateTime.now());
            reservation.setDateFin(LocalDateTime.now().plusHours(1));
            reservation.setStatut(Reservation.Statut.CONFIRMEE);
            reservationService.testSave(reservation);
            LOGGER.info("Test d'enregistrement réussi : " + reservation);
            model.addAttribute("message", "Test d'enregistrement réussi !");
            return "reservations/form";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors du test d'enregistrement : " + e.getMessage());
            model.addAttribute("error", "Erreur lors du test : " + e.getMessage());
            return "reservations/form";
        }
    }

    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable Long id) {
        LOGGER.info("Annulation de la réservation ID : " + id);
        try {
            reservationService.cancelReservation(id);
            return "redirect:/reservations";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'annulation : " + e.getMessage());
            return "redirect:/reservations?error=" + e.getMessage();
        }
    }
}