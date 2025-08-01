package com.labo.reservationsystem.config;

import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.repository.UtilisateurRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordMigration {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void migratePasswords() {
        utilisateurRepository.findAll().forEach(user -> {
            String currentPassword = user.getPassword();
            // Vérifier si le mot de passe n'est pas déjà hashé (BCrypt commence par "$2a$")
            if (!currentPassword.startsWith("$2a$")) {
                String hashedPassword = passwordEncoder.encode(currentPassword);
                user.setPassword(hashedPassword);
                utilisateurRepository.save(user);
                System.out.println("Mot de passe mis à jour pour l'utilisateur : " + user.getEmail());
            }
        });
    }
}
