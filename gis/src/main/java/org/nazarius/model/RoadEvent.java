package org.nazarius.model;

import org.nazarius.VovkDataCore.annotations.Column;
import org.nazarius.VovkDataCore.annotations.Entity;
import org.nazarius.VovkDataCore.annotations.PrimaryKey;
import org.nazarius.VovkDataCore.enums.GenerationType;

import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity(name = "road_events")
public class RoadEvent {

    @PrimaryKey(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "road_id", nullable = false)
    private Integer roadId;

    @Column(name = "measure")
    private Double measure;  // LRS distance along road

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "geom")
    private Point geom;

    @Column(name = "event_date")
    private LocalDateTime date;

    public RoadEvent() {
    }

    public RoadEvent(Integer id, Integer roadId, Double measure, String description, Point geom, LocalDateTime date) {
        this.id = id;
        this.roadId = roadId;
        this.measure = measure;
        this.description = description;
        this.geom = geom;
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

    public Point getGeom() {
        return geom;
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

    public void setGeom(Point geom) {
        this.geom = geom;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}