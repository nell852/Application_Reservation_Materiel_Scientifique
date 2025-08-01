package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.service.UtilisateurService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping({"/", "/signup"})
    public String showSignupForm(Model model) {
        logger.info("Affichage du formulaire d'inscription");
        model.addAttribute("utilisateur", new Utilisateur());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult result,
                               @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        logger.info("Traitement de l'inscription pour l'email : {}", utilisateur.getEmail());
        
        if (result.hasErrors()) {
            logger.warn("Erreurs de validation : {}", result.getAllErrors());
            return "signup";
        }
        
        try {
            utilisateurService.createUtilisateur(utilisateur, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Inscription réussie ! Veuillez vous connecter.");
            logger.info("Inscription réussie pour l'email : {}", utilisateur.getEmail());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            logger.warn("Erreur d'inscription : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/signup";
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'inscription : {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de l'inscription.");
            return "redirect:/signup";
        }
    }
}