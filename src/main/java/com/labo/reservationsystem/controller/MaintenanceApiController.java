package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Maintenance;
import com.labo.reservationsystem.service.MaintenanceService;
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
@RequestMapping("/api/maintenances")
@Tag(name = "Maintenances", description = "API for managing equipment maintenance")
public class MaintenanceApiController {

    @Autowired
    private MaintenanceService maintenanceService;

    @Operation(summary = "Get all maintenances", description = "Retrieves a list of all maintenances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Maintenance> getMaintenances() {
        return maintenanceService.findAll();
    }

    @Operation(summary = "Get maintenance by ID", description = "Retrieves a maintenance by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance found"),
            @ApiResponse(responseCode = "404", description = "Maintenance not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Long id) {
        Optional<Maintenance> maintenance = maintenanceService.findById(id);
        return maintenance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update maintenance", description = "Creates a new maintenance or updates an existing one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance created/updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Maintenance> createMaintenance(@Valid @RequestBody Maintenance maintenance) {
        Maintenance saved = maintenanceService.save(maintenance);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Delete maintenance", description = "Deletes a maintenance by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Maintenance deleted"),
            @ApiResponse(responseCode = "404", description = "Maintenance not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        Optional<Maintenance> maintenance = maintenanceService.findById(id);
        if (maintenance.isPresent()) {
            maintenanceService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}