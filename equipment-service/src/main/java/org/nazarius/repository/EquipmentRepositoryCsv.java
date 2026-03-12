package org.nazarius.repository;

import org.nazarius.VovkDataFiles.csv.CsvEntityManager;
import org.nazarius.VovkDataFiles.csv.CsvFileDescriptor;
import org.nazarius.model.Equipment;

import java.io.FileDescriptor;
import java.util.List;

public class EquipmentRepositoryCsv {
    private final CsvEntityManager csvManager;

    public EquipmentRepositoryCsv(CsvEntityManager csvManager) {
        this.csvManager = csvManager;
    }

    public List<Equipment> findAll() {
        return csvManager.findAll(Equipment.class);
    }

    public boolean register(CsvFileDescriptor descriptor) {
        return csvManager.register(descriptor, Equipment.class);
    }

    public boolean unregister() {
        return csvManager.unregister(Equipment.class);
    }
}
