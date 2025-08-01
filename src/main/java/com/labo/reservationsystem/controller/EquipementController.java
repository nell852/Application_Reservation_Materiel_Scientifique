package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.service.EquipementService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipements")
public class EquipementController {

    private final EquipementService equipementService;

    public EquipementController(EquipementService equipementService) {
        this.equipementService = equipementService;
    }
    
    
    @GetMapping
    public String afficherIndexEquipements() {
        return "equipements/index"; // Correspond à l'emplacement de ta page index.html
    }
    
    @GetMapping("/list")
    public String listEquipements(Model model) {
        model.addAttribute("equipements", equipementService.findAll());
        return "equipements/list";
    }

    @GetMapping("/form")
    public String showEquipementForm(Model model) {
        model.addAttribute("equipement", new Equipement());
        model.addAttribute("etats", Equipement.Etat.values());
        return "equipements/form";
    }

    @PostMapping
    public String saveEquipement(@Valid @ModelAttribute Equipement equipement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("etats", Equipement.Etat.values());
            return "equipements/form";
        }
        equipementService.save(equipement);
        return "redirect:/equipements";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Equipement equipement = equipementService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Équipement non trouvé."));
        model.addAttribute("equipement", equipement);
        model.addAttribute("etats", Equipement.Etat.values());
        return "equipements/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteEquipement(@PathVariable Long id) {
        equipementService.deleteById(id);
        return "redirect:/equipements";
    }
    
    
}