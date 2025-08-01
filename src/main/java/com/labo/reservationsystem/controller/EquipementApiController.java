package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.service.EquipementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipements")
@Tag(name = "Equipements", description = "API for managing scientific equipments")
public class EquipementApiController {

    @Autowired
    private EquipementService equipementService;

    @Operation(summary = "Get all equipments", description = "Retrieves a list of all equipments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Equipement> getEquipements(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return equipementService.findAll().stream()
                    .filter(e -> e.getType().toLowerCase().contains(search.toLowerCase()) ||
                            e.getNumeroSerie().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }
        return equipementService.findAll();
    }

    @Operation(summary = "Get equipment by ID", description = "Retrieves an equipment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment found"),
            @ApiResponse(responseCode = "404", description = "Equipment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Equipement> getEquipementById(@PathVariable Long id) {
        Optional<Equipement> equipement = equipementService.findById(id);
        return equipement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update equipment", description = "Creates a new equipment or updates an existing one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment created/updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Equipement> createEquipement(@Valid @RequestBody Equipement equipement) {
        Equipement saved = equipementService.save(equipement);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Delete equipment", description = "Deletes an equipment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipment deleted"),
            @ApiResponse(responseCode = "404", description = "Equipment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        Optional<Equipement> equipement = equipementService.findById(id);
        if (equipement.isPresent()) {
            equipementService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}