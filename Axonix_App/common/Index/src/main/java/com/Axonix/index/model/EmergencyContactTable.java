package com.Axonix.index.model;

import java.util.Date;

public class EmergencyContactTable {
    private Integer id;

    private Integer userId;

    private Integer contactUserId;

    private String contactPhone;

    private String relationship;

    private Date createdAt;

    private Date updatedAt;

    public EmergencyContactTable(final String contactPhone, final Integer contactUserId, final Date createdAt, final Integer id, final String relationship, final Date updatedAt, final Integer userId) {
        this.contactPhone = contactPhone;
        this.contactUserId = contactUserId;
        this.createdAt = createdAt;
        this.id = id;
        this.relationship = relationship;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public EmergencyContactTable(final Integer contactUserId, final String relationship, final String contactPhone, final Integer userId) {
        this.contactUserId = contactUserId;
        this.relationship = relationship;
        this.contactPhone = contactPhone;
        this.userId = userId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

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

    public Integer getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(Integer contactUserId) {
        this.contactUserId = contactUserId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship == null ? null : relationship.trim();
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
