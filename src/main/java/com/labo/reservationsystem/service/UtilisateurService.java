package com.labo.reservationsystem.service;

import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtilisateurService {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Utilisateur createUtilisateur(Utilisateur utilisateur, String confirmPassword) {
        logger.info("Tentative de création d'un utilisateur avec email : {}", utilisateur.getEmail());
        
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            logger.warn("Email déjà utilisé : {}", utilisateur.getEmail());
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }
        if (!utilisateur.getPassword().equals(confirmPassword)) {
            logger.warn("Les mots de passe ne correspondent pas pour l'email : {}", utilisateur.getEmail());
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas.");
        }
        
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        try {
            Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
            logger.info("Utilisateur créé avec succès : {}", savedUtilisateur.getEmail());
            return savedUtilisateur;
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de l'utilisateur : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur", e);
        }
    }
}