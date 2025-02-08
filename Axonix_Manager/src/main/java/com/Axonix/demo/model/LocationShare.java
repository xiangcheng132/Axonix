package com.Axonix.demo.model;

import java.util.Date;

public class LocationShare {
    private Integer id;

    private Integer userId;

    private Integer sharedWith;

    private Date startTime;

    private Date endTime;

    private Date createdtime;

    private Date updatedtime;

    private String lastLocation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(Integer sharedWith) {
        this.sharedWith = sharedWith;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public Date getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(Date updatedtime) {
        this.updatedtime = updatedtime;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation == null ? null : lastLocation.trim();
    }
}