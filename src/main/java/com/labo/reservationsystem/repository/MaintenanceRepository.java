package com.labo.reservationsystem.repository;

import com.labo.reservationsystem.entity.Maintenance;
import com.labo.reservationsystem.entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByEquipement(Equipement equipement);
}