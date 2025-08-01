package com.labo.reservationsystem.service;

import com.labo.reservationsystem.entity.Maintenance;
import com.labo.reservationsystem.repository.MaintenanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class MaintenanceService {

    private static final Logger LOGGER = Logger.getLogger(MaintenanceService.class.getName());

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Transactional
    public Maintenance save(Maintenance maintenance) {
        LOGGER.info("Tentative d'enregistrement de la maintenance : " + maintenance);
        LOGGER.info("Equipement : " + maintenance.getEquipement());
        if (maintenance.getEquipement() == null) {
            throw new IllegalArgumentException("L'équipement ne peut pas être null.");
        }
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        LOGGER.info("Maintenance enregistrée avec succès : " + savedMaintenance);
        return savedMaintenance;
    }

    @Transactional
    public void deleteById(Long id) {
        LOGGER.info("Suppression de la maintenance ID : " + id);
        maintenanceRepository.deleteById(id);
        LOGGER.info("Maintenance supprimée avec succès");
    }

	public Optional<Maintenance> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}