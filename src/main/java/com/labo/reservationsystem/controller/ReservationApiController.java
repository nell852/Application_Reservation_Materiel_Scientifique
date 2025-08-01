package com.labo.reservationsystem.controller;

import com.labo.reservationsystem.entity.Reservation;
import com.labo.reservationsystem.entity.Utilisateur;
import com.labo.reservationsystem.service.ReservationService;
import com.labo.reservationsystem.repository.UtilisateurRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "API for managing equipment reservations")
public class ReservationApiController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Operation(summary = "Get all reservations", description = "Retrieves a list of reservations (all for GESTIONNAIRE, own for CHERCHEUR)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Reservation> getReservations(Authentication authentication) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        if (utilisateur.getRole() == Utilisateur.Role.GESTIONNAIRE) {
            return reservationService.findAll();
        }
        return reservationService.findByUtilisateur(utilisateur);
    }

    @Operation(summary = "Get reservation by ID", description = "Retrieves a reservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        return reservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update reservation", description = "Creates a new reservation or updates an existing one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation created/updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation, Authentication authentication) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        reservation.setUtilisateur(utilisateur);
        Reservation saved = reservationService.save(reservation);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Cancel reservation", description = "Cancels a reservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation cancelled"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        if (reservation.isPresent()) {
            reservationService.cancelReservation(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}