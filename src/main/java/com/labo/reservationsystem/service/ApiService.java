package com.labo.reservationsystem.service;

import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.entity.Maintenance;
import com.labo.reservationsystem.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ApiService {

    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build();
    }

    public Mono<List<Reservation>> getReservations(String search) {
        String url = search != null && !search.isEmpty() ? 
            "/api/reservations?search=" + search : "/api/reservations";
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Reservation.class)
            .collectList();
    }

    public Mono<List<Maintenance>> getMaintenances(String search) {
        String url = search != null && !search.isEmpty() ? 
            "/api/maintenances?search=" + search : "/api/maintenances";
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Maintenance.class)
            .collectList();
    }

    public Mono<List<Equipement>> getEquipements(String search) {
        String url = search != null && !search.isEmpty() ? 
            "/api/equipements?search=" + search : "/api/equipements";
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Equipement.class)
            .collectList();
    }

    public Mono<Void> deleteReservation(Long id) {
        return webClient.delete()
            .uri("/api/reservations/" + id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> deleteMaintenance(Long id) {
        return webClient.delete()
            .uri("/api/maintenances/" + id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> deleteEquipement(Long id) {
        return webClient.delete()
            .uri("/api/equipements/" + id)
            .retrieve()
            .bodyToMono(Void.class);
    }
}
