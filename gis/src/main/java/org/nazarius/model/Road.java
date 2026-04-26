package org.nazarius.model;


import org.locationtech.jts.geom.LineString;
import org.nazarius.VovkDataCore.annotations.Column;
import org.nazarius.VovkDataCore.annotations.Entity;
import org.nazarius.VovkDataCore.annotations.PrimaryKey;
import org.nazarius.VovkDataCore.enums.GenerationType;
import org.nazarius.VovkDataCore.annotations.Validate;


@Entity(name = "roads")
public class Road {

    @PrimaryKey(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 150, nullable = false)
    @Validate(notBlank = true, minLength = 2, maxLength = 150)
    private String name;

    @Column(name = "geom")
    private LineString geom;

    @Column(name = "length_meters")
    private Double lengthMeters;

    public Road() {
    }

    public Road(Integer id, String name, LineString geom, Double lengthMeters) {
        this.id = id;
        this.name = name;
        this.geom = geom;
        this.lengthMeters = lengthMeters;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LineString getGeom() {
        return geom;
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

    public void setGeom(LineString geom) {
        this.geom = geom;
    }

    public void setLengthMeters(Double lengthMeters) {
        this.lengthMeters = lengthMeters;
    }
}