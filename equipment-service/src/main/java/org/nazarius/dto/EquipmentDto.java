package org.nazarius.dto;

public class EquipmentDto {

    private Integer id;
    private String equipmentNumber;
    private String name;
    private String type;
    private String manufacturer;
    private Integer year;
    private String status;

    public EquipmentDto() {
    }

    public EquipmentDto(Integer id,
                        String equipmentNumber,
                        String name,
                        String type,
                        String manufacturer,
                        Integer year,
                        String status) {
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