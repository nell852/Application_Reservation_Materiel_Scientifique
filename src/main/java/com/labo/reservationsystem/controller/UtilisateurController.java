package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Logger;

@Controller
public class UtilisateurController {

    private static final Logger LOGGER = Logger.getLogger(UtilisateurController.class.getName());

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurController(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/parametres")
    public String showParametres(Model model, Authentication authentication) {
        LOGGER.info("Affichage de la page des paramètres utilisateur");
        try {
            if (authentication == null || authentication.getName() == null) {
                LOGGER.warning("Aucun utilisateur connecté");
                return "redirect:/login";
            }

            Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));
            model.addAttribute("utilisateur", utilisateur);
            return "parametres";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'affichage des paramètres : " + e.getMessage());
            model.addAttribute("errorMessage", "Erreur lors du chargement des paramètres : " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/parametres")
    public String updateParametres(
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            Authentication authentication,
            Model model) {
        LOGGER.info("Mise à jour des paramètres utilisateur");
        try {
            if (authentication == null || authentication.getName() == null) {
                LOGGER.warning("Aucun utilisateur connecté");
                return "redirect:/login";
            }

            Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));

            // Vérifier si l'email est déjà utilisé par un autre utilisateur
            if (!utilisateur.getEmail().equals(email) && utilisateurRepository.findByEmail(email).isPresent()) {
                model.addAttribute("errorMessage", "Cet email est déjà utilisé.");
                model.addAttribute("utilisateur", utilisateur);
                return "parametres";
            }

            // Mettre à jour les informations
            utilisateur.setNom(nom.trim());
            utilisateur.setEmail(email.trim());

            // Mettre à jour le mot de passe si fourni
            if (password != null && !password.isEmpty()) {
                utilisateur.setMotDePasse(passwordEncoder.encode(password));
                LOGGER.info("Mot de passe mis à jour pour l'utilisateur : " + utilisateur.getEmail());
            }

            utilisateurRepository.save(utilisateur);
            LOGGER.info("Paramètres mis à jour pour l'utilisateur : " + utilisateur.getEmail());

            model.addAttribute("successMessage", "Modifications enregistrées avec succès.");
            model.addAttribute("utilisateur", utilisateur);
            return "parametres";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la mise à jour des paramètres : " + e.getMessage());
            model.addAttribute("errorMessage", "Erreur lors de la mise à jour : " + e.getMessage());
            Object utilisateur = null;
			model.addAttribute("utilisateur", utilisateur);
            return "parametres";
        }
    }
}