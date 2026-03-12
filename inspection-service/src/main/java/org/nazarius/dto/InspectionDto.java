package org.nazarius.dto;

import java.time.LocalDate;

public class InspectionDto {

    private Integer id;
    private String inspectionCode;
    private String title;
    private String description;
    private String inspectionType;
    private String location;
    private LocalDate scheduledDate;
    private LocalDate completedDate;
    private String status;
    private String inspector;

    public InspectionDto() {
    }

    public InspectionDto(Integer id,
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