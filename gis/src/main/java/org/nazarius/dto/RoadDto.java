package org.nazarius.dto;

public class RoadDto {

    private Integer id;
    private String name;
    private String geomWkt;   // geometry as WKT for API transfer
    private Double lengthMeters;

    public RoadDto() {
    }

    public RoadDto(Integer id, String name, String geomWkt, Double lengthMeters) {
        this.id = id;
        this.name = name;
        this.geomWkt = geomWkt;
        this.lengthMeters = lengthMeters;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGeomWkt() {
        return geomWkt;
    }

    public Double getLengthMeters() {
        return lengthMeters;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGeomWkt(String geomWkt) {
        this.geomWkt = geomWkt;
    }

    public void setLengthMeters(Double lengthMeters) {
        this.lengthMeters = lengthMeters;
    }
}