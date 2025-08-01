package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Maintenance;
import com.labo.reservationsystem.service.EquipementService;
import com.labo.reservationsystem.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RequestMapping("/maintenances")
public class MaintenanceController {

    private static final Logger LOGGER = Logger.getLogger(MaintenanceController.class.getName());

    private final MaintenanceService maintenanceService;
    private final EquipementService equipementService;

    public MaintenanceController(MaintenanceService maintenanceService, EquipementService equipementService) {
        this.maintenanceService = maintenanceService;
        this.equipementService = equipementService;
    }
    
    
    @GetMapping
    public String afficherIndexMaintenances() {
        return "maintenances/index"; // Correspond à maintenances/index.html
    }


    @GetMapping("/list")
    public String listMaintenances(Model model) {
        LOGGER.info("Affichage de la liste des maintenances");
        try {
            model.addAttribute("maintenances", maintenanceService.findAll());
            return "maintenances/list";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'affichage des maintenances : " + e.getMessage());
            model.addAttribute("error", "Erreur lors du chargement des maintenances : " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/form")
    public String showMaintenanceForm(Model model) {
        LOGGER.info("Affichage du formulaire de maintenance");
        try {
            model.addAttribute("maintenance", new Maintenance());
            model.addAttribute("equipements", equipementService.findAll());
            return "maintenances/form";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'affichage du formulaire : " + e.getMessage());
            model.addAttribute("error", "Erreur lors du chargement du formulaire : " + e.getMessage());
            return "error";
        }
    }

    @PostMapping
    public String saveMaintenance(@Valid @ModelAttribute Maintenance maintenance, BindingResult result, Model model) {
        LOGGER.info("Tentative d'enregistrement de la maintenance : " + maintenance);
        if (result.hasErrors()) {
            LOGGER.warning("Erreurs de validation : " + result.getAllErrors());
            model.addAttribute("equipements", equipementService.findAll());
            return "maintenances/form";
        }
        try {
            maintenanceService.save(maintenance);
            LOGGER.info("Maintenance enregistrée avec succès : " + maintenance);
            return "redirect:/maintenances";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("equipements", equipementService.findAll());
            return "maintenances/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id) {
        LOGGER.info("Suppression de la maintenance ID : " + id);
        try {
            maintenanceService.deleteById(id);
            return "redirect:/maintenances";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la suppression : " + e.getMessage());
            return "redirect:/maintenances?error=" + e.getMessage();
        }
    }
}