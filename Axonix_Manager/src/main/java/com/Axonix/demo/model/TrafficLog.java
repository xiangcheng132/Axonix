package com.Axonix.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class TrafficLog {
    private Integer id;

    private Integer userId;

    private BigDecimal lastLng;

    private BigDecimal lastLat;

    private Date startTime;

    private Date endTime;

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

    public BigDecimal getLastLng() {
        return lastLng;
    }

    public void setLastLng(BigDecimal lastLng) {
        this.lastLng = lastLng;
    }

    public BigDecimal getLastLat() {
        return lastLat;
    }

    public void setLastLat(BigDecimal lastLat) {
        this.lastLat = lastLat;
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
}