package org.nazarius.mapper;

import org.nazarius.model.Equipment;
import org.nazarius.dto.EquipmentDto;

public class EquipmentMapper {

    // Convert Equipment entity to EquipmentDto
    public static EquipmentDto toDto(Equipment equipment) {
        if (equipment == null) return null;

        return new EquipmentDto(
                equipment.getId(),
                equipment.getEquipmentNumber(),
                equipment.getName(),
                equipment.getType(),
                equipment.getManufacturer(),
                equipment.getYear(),
                equipment.getStatus()
        );
    }

    // Convert EquipmentDto to Equipment entity
    public static Equipment toEntity(EquipmentDto dto) {
        if (dto == null) return null;

        return new Equipment(
                dto.getId(),
                dto.getEquipmentNumber(),
                dto.getName(),
                dto.getType(),
                dto.getManufacturer(),
                dto.getYear(),
                dto.getStatus()
        );
    }
}