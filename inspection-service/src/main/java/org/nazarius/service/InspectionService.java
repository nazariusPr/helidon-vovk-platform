package org.nazarius.service;

import org.nazarius.dto.InspectionDto;
import org.nazarius.model.Inspection;
import org.nazarius.repository.InspectionRepositoryDb;
import org.nazarius.repository.InspectionRepositoryInMemory;
import org.nazarius.mapper.InspectionMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InspectionService {

    private final InspectionRepositoryDb dbRepository;
    private final InspectionRepositoryInMemory inMemoryRepository;

    public InspectionService(
            InspectionRepositoryDb dbRepository,
            InspectionRepositoryInMemory inMemoryRepository
    ) {
        this.dbRepository = dbRepository;
        this.inMemoryRepository = inMemoryRepository;
    }

    /* =========================
       READ
       ========================= */

    public List<InspectionDto> findAll() {
        List<Inspection> dbList = dbRepository.findAll();
        List<Inspection> memList = inMemoryRepository.findAll();

        Map<Integer, Inspection> merged = new LinkedHashMap<>();
        for (Inspection e : dbList) merged.put(e.getId(), e);
        for (Inspection e : memList) merged.put(e.getId(), e); // in-memory overrides DB

        return merged.values().stream()
                .map(InspectionMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<InspectionDto> findById(Integer id) {
        Optional<Inspection> mem = inMemoryRepository.findById(id);
        Inspection entity = mem.orElseGet(() -> dbRepository.findById(id).orElse(null));
        return Optional.ofNullable(InspectionMapper.toDto(entity));
    }

    public Optional<InspectionDto> findByInspectionCode(String code) {
        Optional<Inspection> mem = inMemoryRepository.findByInspectionCode(code);
        Inspection entity = mem.orElseGet(() -> dbRepository.findByInspectionCode(code).orElse(null));
        return Optional.ofNullable(InspectionMapper.toDto(entity));
    }

    /* =========================
       WRITE
       ========================= */

    public int save(InspectionDto dto) {
        Inspection entity = InspectionMapper.toEntity(dto);
        int saved = inMemoryRepository.save(entity);

        if (saved == 0) {
            throw new IllegalStateException(
                    "Failed to create inspection. Affected rows: " + saved
            );
        }
        Inspection savedInspection = inMemoryRepository.findByInspectionCode(dto.getInspectionCode())
                .orElseThrow(() -> new IllegalStateException("Inspection was saved but not found"));
        return savedInspection.getId();
    }

    public int update(InspectionDto dto) {
        Inspection entity = InspectionMapper.toEntity(dto);
        int mem = inMemoryRepository.update(entity);
        int db = dbRepository.update(entity);
        return mem + db;
    }

    // Delete only in-memory
    public int deleteInMemoryById(Integer id) {
        return inMemoryRepository.deleteById(id);
    }

    // Delete only in database
    public int deleteInDbById(Integer id) {
        return dbRepository.deleteById(id);
    }

    // Delete all only in-memory
    public int deleteAllInMemory() {
        return inMemoryRepository.deleteAll();
    }

    // Delete all only in database
    public int deleteAllInDb() {
        return dbRepository.deleteAll();
    }


    /* =========================
       FLUSH
       ========================= */

    public int flushToDatabase() {
        List<Inspection> memList = inMemoryRepository.findAll();
        if (memList.isEmpty()) return 0;

        int affected = 0;
        for (Inspection e : memList) {
            dbRepository.save(e);
            inMemoryRepository.deleteById(e.getId());
            affected++;
        }
        return affected;
    }
}