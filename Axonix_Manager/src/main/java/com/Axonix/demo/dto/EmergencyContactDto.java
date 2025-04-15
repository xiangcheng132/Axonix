package com.Axonix.demo.dto;

import java.util.Date;

public class EmergencyContactDto {
    private Integer id;
    private Integer userId;
    private Integer contactUserId;
    private String contactPhone;
    private String relationship;
    private Date createdAt;
    private Date updatedAt;
    private String contactUsername;

    // Getter 和 Setter 方法

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
        this.contactPhone = contactPhone;
    }
    public String getRelationship() {
        return relationship;
    }
    public void setRelationship(String relationship) {
        this.relationship = relationship;
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
    public String getContactUsername() {
        return contactUsername;
    }
    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }
}
