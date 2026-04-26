package org.nazarius.model;

import org.nazarius.VovkDataCore.annotations.Column;
import org.nazarius.VovkDataCore.annotations.Entity;
import org.nazarius.VovkDataCore.annotations.PrimaryKey;
import org.nazarius.VovkDataCore.enums.GenerationType;

import org.locationtech.jts.geom.LineString;

@Entity(name = "road_segments")
public class RoadSegment {

    @PrimaryKey(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "road_id", nullable = false)
    private Integer roadId;

    @Column(name = "segment_index")
    private Integer segmentIndex;

    @Column(name = "geom")
    private LineString geom;

    @Column(name = "start_measure")
    private Double startMeasure;

    @Column(name = "end_measure")
    private Double endMeasure;

    public RoadSegment() {
    }

    public RoadSegment(Integer id, Integer roadId, Integer segmentIndex,
                       LineString geom, Double startMeasure, Double endMeasure) {
        this.id = id;
        this.roadId = roadId;
        this.segmentIndex = segmentIndex;
        this.geom = geom;
        this.startMeasure = startMeasure;
        this.endMeasure = endMeasure;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRoadId() {
        return roadId;
    }

    public Integer getSegmentIndex() {
        return segmentIndex;
    }

    public LineString getGeom() {
        return geom;
    }

    public Double getStartMeasure() {
        return startMeasure;
    }

    public Double getEndMeasure() {
        return endMeasure;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoadId(Integer roadId) {
        this.roadId = roadId;
    }

    public void setSegmentIndex(Integer segmentIndex) {
        this.segmentIndex = segmentIndex;
    }

    public void setGeom(LineString geom) {
        this.geom = geom;
    }

    public void setStartMeasure(Double startMeasure) {
        this.startMeasure = startMeasure;
    }

    public void setEndMeasure(Double endMeasure) {
        this.endMeasure = endMeasure;
    }
}
