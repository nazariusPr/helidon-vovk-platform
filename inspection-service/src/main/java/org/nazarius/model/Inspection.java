package org.nazarius.model;

import org.nazarius.VovkDataCore.annotations.Column;
import org.nazarius.VovkDataCore.annotations.Entity;
import org.nazarius.VovkDataCore.annotations.PrimaryKey;
import org.nazarius.VovkDataCore.annotations.Validate;
import org.nazarius.VovkDataCore.enums.GenerationType;

import java.time.LocalDate;

@Entity(name = "inspections")
public class Inspection {

    @PrimaryKey(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "inspection_code", unique = true, length = 100)
    @Validate(notBlank = true, minLength = 3, maxLength = 100)
    private String inspectionCode;

    @Column(name = "title", length = 150)
    @Validate(notBlank = true, minLength = 3, maxLength = 150)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "inspection_type", length = 100)
    @Validate(notBlank = true)
    private String inspectionType;

    @Column(name = "location", length = 200)
    @Validate(notBlank = true, minLength = 2, maxLength = 200)
    private String location;

    @Column(name = "scheduled_date")
    @Validate(notNull = true)
    private LocalDate scheduledDate;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Column(name = "status", length = 50)
    @Validate(notBlank = true)
    private String status;

    @Column(name = "inspector", length = 150)
    @Validate(notBlank = true, minLength = 2, maxLength = 150)
    private String inspector;

    public Inspection() {
    }

    public Inspection(Integer id,
                      String inspectionCode,
                      String title,
                      String description,
                      String inspectionType,
                      String location,
                      LocalDate scheduledDate,
                      LocalDate completedDate,
                      String status,
                      String inspector) {
        this.id = id;
        this.inspectionCode = inspectionCode;
        this.title = title;
        this.description = description;
        this.inspectionType = inspectionType;
        this.location = location;
        this.scheduledDate = scheduledDate;
        this.completedDate = completedDate;
        this.status = status;
        this.inspector = inspector;
    }

    public Integer getId() {
        return id;
    }

    public String getInspectionCode() {
        return inspectionCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public String getStatus() {
        return status;
    }

    public String getInspector() {
        return inspector;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }
}