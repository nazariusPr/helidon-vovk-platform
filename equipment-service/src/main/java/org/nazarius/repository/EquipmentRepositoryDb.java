package org.nazarius.repository;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.VovkORM.sql.builder.Delete;
import org.nazarius.VovkORM.sql.builder.Select;
import org.nazarius.model.Equipment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.nazarius.VovkORM.sql.builder.Select.select;
import static org.nazarius.VovkORM.sql.common.Where.column;

public class EquipmentRepositoryDb {

    private final JdbcEntityManager manager;

    public EquipmentRepositoryDb(JdbcEntityManager manager) {
        this.manager = manager;
    }

    public Optional<Equipment> findById(Integer id) {
        return Optional.ofNullable(manager.findById(id, Equipment.class));
    }

    public Optional<Equipment> findByEquipmentNumber(String equipmentNumber) {
        Select select = select()
                .from("equipments")
                .where(column("equipment_number").eq(equipmentNumber));

        List<Equipment> equipments = manager.find(select, Equipment.class);

        if (equipments.isEmpty()) {
            return Optional.empty();
        } else if (equipments.size() > 1) {
            throw new IllegalStateException(
                    "Multiple equipments found with equipment_number: " + equipmentNumber
            );
        } else {
            return Optional.of(equipments.getFirst());
        }
    }

    public List<Equipment> findAll() {
        return manager.findAll(Equipment.class);
    }

    public Page<Equipment> readPage(Pageable pageable) {
        return manager.readPage(pageable, Equipment.class);
    }

    public int save(Equipment equipment) {
        return manager.save(equipment, Equipment.class);
    }

    public int saveAll(Collection<Equipment> equipments) {
        return manager.saveAll(equipments, Equipment.class);
    }

    public int update(Equipment equipment) {
        return manager.update(equipment, Equipment.class);
    }

    public int deleteById(Integer id) {
        return manager.deleteById(id, Equipment.class);
    }

    public int deleteAll() {
        return manager.deleteAll(Equipment.class);
    }
}