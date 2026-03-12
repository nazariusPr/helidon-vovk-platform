package org.nazarius.repository;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.VovkDataInMemory.api.InMemoryEntityManager;
import org.nazarius.model.Inspection;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InspectionRepositoryInMemory {

    private final InMemoryEntityManager manager;

    public InspectionRepositoryInMemory(InMemoryEntityManager manager) {
        this.manager = manager;
    }

    public Optional<Inspection> findById(Integer id) {
        return Optional.ofNullable(manager.findById(id, Inspection.class));
    }

    public Optional<Inspection> findByInspectionCode(String inspectionCode) {
        List<Inspection> inspections = manager.findAll(Inspection.class);

        List<Inspection> filtered = inspections.stream()
                .filter(i -> inspectionCode.equals(i.getInspectionCode()))
                .toList();

        if (filtered.isEmpty()) {
            return Optional.empty();
        } else if (filtered.size() > 1) {
            throw new IllegalStateException(
                    "Multiple inspections found with inspection_code: " + inspectionCode
            );
        } else {
            return Optional.of(filtered.getFirst());
        }
    }

    public List<Inspection> findAll() {
        return manager.findAll(Inspection.class);
    }

    public Page<Inspection> readPage(Pageable pageable) {
        return manager.readPage(pageable, Inspection.class);
    }

    public int save(Inspection inspection) {
        return manager.save(inspection, Inspection.class);
    }

    public int saveAll(Collection<Inspection> inspections) {
        return manager.saveAll(inspections, Inspection.class);
    }

    public int update(Inspection inspection) {
        return manager.update(inspection, Inspection.class);
    }

    public int deleteById(Integer id) {
        return manager.deleteById(id, Inspection.class);
    }

    public int deleteAll() {
        return manager.deleteAll(Inspection.class);
    }
}