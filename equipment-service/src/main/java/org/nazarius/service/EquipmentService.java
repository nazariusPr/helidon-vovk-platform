package org.nazarius.service;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.VovkDataFiles.csv.CsvFileDescriptor;
import org.nazarius.dto.EquipmentDto;
import org.nazarius.mapper.EquipmentMapper;
import org.nazarius.model.Equipment;
import org.nazarius.repository.EquipmentRepositoryCsv;
import org.nazarius.repository.EquipmentRepositoryDb;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EquipmentService {

    private final EquipmentRepositoryDb repositoryDb;
    private final EquipmentRepositoryCsv repositoryCsv;

    public EquipmentService(EquipmentRepositoryDb repositoryDb, EquipmentRepositoryCsv repositoryCsv) {
        this.repositoryDb = repositoryDb;
        this.repositoryCsv = repositoryCsv;
    }

    // Fetch all equipments as DTOs
    public List<EquipmentDto> getAllEquipments() {
        List<Equipment> equipments = repositoryDb.findAll();
        if (equipments == null || equipments.isEmpty()) {
            return Collections.emptyList();
        }

        return equipments.stream()
                .map(EquipmentMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetch paged equipments
    public Page<EquipmentDto> readPage(Pageable pageable) {

        Page<Equipment> equipmentPage = repositoryDb.readPage(pageable);

        List<EquipmentDto> dtoList = equipmentPage.getElems().stream()
                .map(EquipmentMapper::toDto)
                .collect(Collectors.toList());

        return new Page<>(
                dtoList,
                equipmentPage.getCurrentPage(),
                equipmentPage.getTotalElems(),
                equipmentPage.getTotalPages()
        );
    }

    // Fetch equipment by ID
    public EquipmentDto getById(Integer id) {
        Equipment equipment = repositoryDb.findById(id).orElse(null);
        return equipment != null ? EquipmentMapper.toDto(equipment) : null;
    }

    // Fetch equipment by equipment number
    public EquipmentDto getByEquipmentNumber(String equipmentNumber) {
        Equipment equipment = repositoryDb.findByEquipmentNumber(equipmentNumber).orElse(null);
        return equipment != null ? EquipmentMapper.toDto(equipment) : null;
    }

    // Create new equipment
    public EquipmentDto create(EquipmentDto dto) {

        Equipment equipment = EquipmentMapper.toEntity(dto);

        int saved = repositoryDb.save(equipment);

        if (saved == 0) {
            throw new IllegalStateException(
                    "Failed to create equipment. Affected rows: " + saved
            );
        }

        Equipment savedEquipment = repositoryDb
                .findByEquipmentNumber(dto.getEquipmentNumber())
                .orElseThrow(() ->
                        new IllegalStateException("Equipment was saved but not found")
                );

        return EquipmentMapper.toDto(savedEquipment);
    }

    // Update equipment
    public EquipmentDto update(EquipmentDto dto) {

        if (dto.getId() == null) {
            throw new IllegalStateException("Equipment ID must not be null for update");
        }

        Equipment entity = EquipmentMapper.toEntity(dto);

        int affected = repositoryDb.update(entity);

        if (affected == 0) {
            throw new IllegalStateException(
                    "Equipment with ID " + dto.getId() + " does not exist"
            );
        }

        // Re-fetch the persisted state after update
        Equipment refreshed = repositoryDb.findById(dto.getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Equipment updated but not found with ID " + dto.getId()
                        )
                );

        return EquipmentMapper.toDto(refreshed);
    }

    // Delete equipment
    public void deleteById(Integer id) {

        boolean exists = repositoryDb.findById(id).isPresent();
        if (!exists) {
            throw new IllegalStateException("Equipment with ID " + id + " does not exist");
        }

        int deleted = repositoryDb.deleteById(id);

        if (deleted == 0) {
            throw new IllegalStateException("Failed to delete equipment with ID " + id);
        }
    }

    public int deleteAll() {
        return repositoryDb.deleteAll();
    }

    public int uploadCsv(byte[] csvBytes, boolean hasHeader, char delimiter) {
        if (csvBytes == null || csvBytes.length == 0) {
            throw new IllegalArgumentException("CSV content is empty");
        }

        CsvFileDescriptor descriptor = new CsvFileDescriptor(csvBytes, hasHeader, delimiter);
        boolean registered = repositoryCsv.register(descriptor);
        if (!registered) {
            throw new IllegalStateException("CSV file could not be registered");
        }

        try {
            List<Equipment> equipmentList = repositoryCsv.findAll();
            if (equipmentList.isEmpty()) {
                return 0;
            }

            return repositoryDb.saveAll(equipmentList);

        } finally {
            repositoryCsv.unregister();
        }
    }
}