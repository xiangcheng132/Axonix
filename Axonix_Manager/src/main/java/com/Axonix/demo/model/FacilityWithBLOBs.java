package com.Axonix.demo.model;

public class FacilityWithBLOBs extends Facility {
    private String location;

    private String description;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}