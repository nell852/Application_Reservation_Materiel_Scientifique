package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.entity.Reservation;
import com.labo.reservationsystem.entity.Maintenance;
import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.repository.EquipementRepository;
import com.labo.reservationsystem.repository.ReservationRepository;
import com.labo.reservationsystem.repository.MaintenanceRepository;
import com.labo.reservationsystem.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Controller
public class HomeController {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());

    private final EquipementRepository equipementRepository;
    private final ReservationRepository reservationRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final UtilisateurRepository utilisateurRepository;

    public HomeController(EquipementRepository equipementRepository, ReservationRepository reservationRepository,
                          MaintenanceRepository maintenanceRepository, UtilisateurRepository utilisateurRepository) {
        this.equipementRepository = equipementRepository;
        this.reservationRepository = reservationRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        LOGGER.info("Chargement du tableau de bord");
        try {
            // Récupérer l'utilisateur connecté
            Utilisateur utilisateur = null;
            if (authentication != null && authentication.getName() != null) {
                utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                        .orElse(null);
                LOGGER.info("Utilisateur connecté : " + (utilisateur != null ? utilisateur.getEmail() : "inconnu"));
            }
            model.addAttribute("utilisateur", utilisateur);

            List<Equipement> equipements = equipementRepository.findAll();
            List<Reservation> reservations = reservationRepository.findAll();
            List<Maintenance> maintenances = maintenanceRepository.findAll();

            LOGGER.info("Nombre de réservations récupérées : " + reservations.size());
            reservations.forEach(r -> LOGGER.info("Réservation : utilisateur=" + r.getUtilisateur().getEmail() + ", rôle=" + r.getUtilisateur().getRole() + ", statut=" + r.getStatut()));

            // Statistiques
            long totalEquipements = equipements.size();
            LocalDateTime now = LocalDateTime.now();
            long reservationsActives = reservations.stream()
                    .filter(r -> r.getDateDebut().isBefore(now) && r.getDateFin().isAfter(now)
                            && r.getStatut() == Reservation.Statut.CONFIRMEE)
                    .count();

            Map<String, Long> repartitionParType = equipements.stream()
                    .collect(Collectors.groupingBy(Equipement::getType, Collectors.counting()));

            long disponibles = equipements.stream()
                    .filter(e -> e.getEtat() == Equipement.Etat.DISPONIBLE)
                    .count();
            long enMaintenance = equipements.stream()
                    .filter(e -> e.getEtat() == Equipement.Etat.EN_MAINTENANCE)
                    .count();
            long enUtilisation = totalEquipements - disponibles - enMaintenance;

            // Top chercheurs
            Map<String, Long> activiteUtilisateurs = reservations.stream()
                    .filter(r -> r.getUtilisateur().getRole() == Utilisateur.Role.CHERCHEUR)
                    .collect(Collectors.groupingBy(
                            r -> r.getUtilisateur().getNom() + " (" + r.getUtilisateur().getEmail() + ")",
                            Collectors.counting()
                    ));

            List<Map.Entry<String, Long>> topChercheurs = activiteUtilisateurs.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(5)
                    .toList();

            LOGGER.info("Top chercheurs : " + topChercheurs);

            // Notifications
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime inThreeDays = today.plusDays(3);

            List<Reservation> bientotLibres = reservations.stream()
                    .filter(r -> r.getDateFin().isAfter(today) && r.getDateFin().isBefore(inThreeDays)
                            && r.getStatut() == Reservation.Statut.CONFIRMEE)
                    .toList();

            List<Reservation> seTerminantAujourdhui = reservations.stream()
                    .filter(r -> r.getDateFin().toLocalDate().isEqual(today.toLocalDate())
                            && r.getStatut() == Reservation.Statut.CONFIRMEE)
                    .toList();

            // Taux d'occupation par équipement (30 derniers jours)
            Map<String, Double> tauxOccupationParEquipement = new HashMap<>();
            LocalDateTime ilY30Jours = today.minusDays(30);

            for (Equipement eq : equipements) {
                List<Reservation> resEq = reservations.stream()
                        .filter(r -> r.getEquipement().getId().equals(eq.getId())
                                && r.getStatut() == Reservation.Statut.CONFIRMEE)
                        .toList();

                long heuresReservees = resEq.stream()
                        .mapToLong(r -> {
                            LocalDateTime debut = r.getDateDebut().isBefore(ilY30Jours) ? ilY30Jours : r.getDateDebut();
                            LocalDateTime fin = r.getDateFin().isAfter(today) ? today : r.getDateFin();
                            return Math.max(0, ChronoUnit.HOURS.between(debut, fin));
                        })
                        .sum();

                double taux = (heuresReservees / (30.0 * 24)) * 100; // 30 jours * 24 heures
                tauxOccupationParEquipement.put(eq.getType() + " (" + eq.getNumeroSerie() + ")", Math.min(taux, 100.0));
            }

            // Fréquence des maintenances par équipement
            Map<String, Long> frequenceMaintenances = maintenances.stream()
                    .collect(Collectors.groupingBy(
                            m -> m.getEquipement().getType() + " (" + m.getEquipement().getNumeroSerie() + ")",
                            Collectors.counting()
                    ));

            LOGGER.info("Fréquence des maintenances : " + frequenceMaintenances);

            // Injection dans le modèle
            model.addAttribute("totalEquipements", totalEquipements);
            model.addAttribute("reservationsActives", reservationsActives);
            model.addAttribute("repartitionParType", repartitionParType);
            model.addAttribute("disponibles", disponibles);
            model.addAttribute("enMaintenance", enMaintenance);
            model.addAttribute("enUtilisation", enUtilisation);
            model.addAttribute("topChercheurs", topChercheurs);
            model.addAttribute("maintenances", maintenances);
            model.addAttribute("bientotLibres", bientotLibres);
            model.addAttribute("seTerminantAujourdhui", seTerminantAujourdhui);
            model.addAttribute("tauxOccupationParEquipement", tauxOccupationParEquipement);
            model.addAttribute("frequenceMaintenances", frequenceMaintenances);

            return "home";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors du chargement du tableau de bord : " + e.getMessage());
            model.addAttribute("error", "Erreur lors du chargement du tableau de bord : " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}