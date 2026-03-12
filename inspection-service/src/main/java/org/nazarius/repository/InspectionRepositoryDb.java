package org.nazarius.repository;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.VovkORM.sql.builder.Select;
import org.nazarius.model.Inspection;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.nazarius.VovkORM.sql.builder.Select.select;
import static org.nazarius.VovkORM.sql.common.Where.column;

public class InspectionRepositoryDb {

    private final JdbcEntityManager manager;

    public InspectionRepositoryDb(JdbcEntityManager manager) {
        this.manager = manager;
    }

    public Optional<Inspection> findById(Integer id) {
        return Optional.ofNullable(manager.findById(id, Inspection.class));
    }

    public Optional<Inspection> findByInspectionCode(String inspectionCode) {
        Select select = select()
                .from("inspections")
                .where(column("inspection_code").eq(inspectionCode));

        List<Inspection> inspections = manager.find(select, Inspection.class);

        if (inspections.isEmpty()) {
            return Optional.empty();
        } else if (inspections.size() > 1) {
            throw new IllegalStateException(
                    "Multiple inspections found with inspection_code: " + inspectionCode
            );
        } else {
            return Optional.of(inspections.getFirst());
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