package org.nazarius.dto;

import java.time.LocalDateTime;

public class RoadEventDto {

    private Integer id;
    private Integer roadId;
    private Double measure;          // LRS measure
    private String description;
    private String geomWkt;          // geometry as WKT
    private LocalDateTime date;

    public RoadEventDto() {
    }

    public RoadEventDto(Integer id, Integer roadId, Double measure, String description,
                        String geomWkt, LocalDateTime date) {
        this.id = id;
        this.roadId = roadId;
        this.measure = measure;
        this.description = description;
        this.geomWkt = geomWkt;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRoadId() {
        return roadId;
    }

    public Double getMeasure() {
        return measure;
    }

    public String getDescription() {
        return description;
    }

    public String getGeomWkt() {
        return geomWkt;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoadId(Integer roadId) {
        this.roadId = roadId;
    }

    public void setMeasure(Double measure) {
        this.measure = measure;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeomWkt(String geomWkt) {
        this.geomWkt = geomWkt;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}