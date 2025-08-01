package com.labo.reservationsystem.service;

import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.repository.EquipementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;

    public EquipementService(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    public Optional<Equipement> findById(Long id) {
        return equipementRepository.findById(id);
    }

    public Equipement save(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    public void deleteById(Long id) {
        equipementRepository.deleteById(id);
    }
}