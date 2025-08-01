package com.labo.reservationsystem.converter;

import com.labo.reservationsystem.entity.Equipement;
import com.labo.reservationsystem.repository.EquipementRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EquipementConverter implements Converter<String, Equipement> {

    private final EquipementRepository equipementRepository;

    public EquipementConverter(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    @Override
    public Equipement convert(String source) {
        try {
            Long id = Long.parseLong(source);
            return equipementRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
