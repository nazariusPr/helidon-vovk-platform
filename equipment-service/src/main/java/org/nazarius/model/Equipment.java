package org.nazarius.model;


import org.nazarius.VovkDataCore.annotations.Column;
import org.nazarius.VovkDataCore.annotations.Entity;
import org.nazarius.VovkDataCore.annotations.PrimaryKey;
import org.nazarius.VovkDataCore.annotations.Validate;
import org.nazarius.VovkDataCore.enums.GenerationType;

@Entity(name = "equipments")
public class Equipment {
    @PrimaryKey(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "equipment_number", unique = true, length = 100)
    @Validate(notBlank = true, minLength = 2, maxLength = 100)
    private String equipmentNumber;

    @Column(name = "name", length = 150)
    @Validate(notBlank = true, minLength = 2, maxLength = 150)
    private String name;

    @Column(name = "type", length = 100)
    @Validate(notBlank = true)
    private String type;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "year")
    @Validate(min = 1900)
    private Integer year;

    @Column(name = "status", length = 50)
    @Validate(notBlank = true)
    private String status;

    public Equipment() {
    }

    public Equipment(Integer id, String equipmentNumber, String name,
                     String type, String manufacturer, Integer year, String status) {
        this.id = id;
        this.equipmentNumber = equipmentNumber;
        this.name = name;
        this.type = type;
        this.manufacturer = manufacturer;
        this.year = year;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getYear() {
        return year;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
