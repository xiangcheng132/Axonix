package com.Axonix.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class HelpRequest {
    private Integer id;

    private Integer requesterId;

    private Integer helperId;

    private BigDecimal requesterLng;

    private BigDecimal requesterLat;

    private BigDecimal helperLng;

    private BigDecimal helperLat;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public Integer getHelperId() {
        return helperId;
    }

    public void setHelperId(Integer helperId) {
        this.helperId = helperId;
    }

    public BigDecimal getRequesterLng() {
        return requesterLng;
    }

    public void setRequesterLng(BigDecimal requesterLng) {
        this.requesterLng = requesterLng;
    }

    public BigDecimal getRequesterLat() {
        return requesterLat;
    }

    public void setRequesterLat(BigDecimal requesterLat) {
        this.requesterLat = requesterLat;
    }

    public BigDecimal getHelperLng() {
        return helperLng;
    }

    public void setHelperLng(BigDecimal helperLng) {
        this.helperLng = helperLng;
    }

    public BigDecimal getHelperLat() {
        return helperLat;
    }

    public void setHelperLat(BigDecimal helperLat) {
        this.helperLat = helperLat;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}