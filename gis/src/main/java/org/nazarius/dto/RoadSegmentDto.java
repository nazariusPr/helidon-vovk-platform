package org.nazarius.dto;

public class RoadSegmentDto {

    private Integer id;
    private Integer roadId;
    private Integer segmentIndex;
    private String geomWkt;      // geometry as WKT
    private Double startMeasure;
    private Double endMeasure;

    public RoadSegmentDto() {
    }

    public RoadSegmentDto(Integer id, Integer roadId, Integer segmentIndex, String geomWkt,
                          Double startMeasure, Double endMeasure) {
        this.id = id;
        this.roadId = roadId;
        this.segmentIndex = segmentIndex;
        this.geomWkt = geomWkt;
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

    public String getGeomWkt() {
        return geomWkt;
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

    public void setGeomWkt(String geomWkt) {
        this.geomWkt = geomWkt;
    }

    public void setStartMeasure(Double startMeasure) {
        this.startMeasure = startMeasure;
    }

    public void setEndMeasure(Double endMeasure) {
        this.endMeasure = endMeasure;
    }
}