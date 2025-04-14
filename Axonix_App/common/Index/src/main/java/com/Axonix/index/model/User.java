package com.Axonix.index.model;

import java.util.Date;

public class User {
    private Integer id;

    private String username;

    private String password;

    private String avatar;

    private Integer gender;

    private Integer age;

    private String province;

    private String city;

    private String county;

    private String address;

    private String email;

    private String phone;

    private Integer disabilityType;

    private Date lastLoginTime;

    private Integer isVip;

    private Date createdAt;

    private Date updatedAt;

    private String deviceToken;

    public User() {
        this.username = "Axonix_user";
        this.gender = 0;
        this.age = 18;
        this.province = "浙江省";
        this.city = "温州市";
        this.county = "龙港市";
        this.address = "1号";
        this.email = "default@qq.com";
        this.phone = "15000000000";
        this.disabilityType = 0;
        this.isVip = 0;
        this.createdAt = new Date();
    }

    public User(Integer id, String username, String password, String avatar, Integer gender, Integer age, String province, String city, String county, String address, String email, String phone, Integer disabilityType, Date lastLoginTime, Integer isVip, Date createdAt, Date updatedAt, String deviceToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.gender = gender;
        this.age = age;
        this.province = province;
        this.city = city;
        this.county = county;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.disabilityType = disabilityType;
        this.lastLoginTime = lastLoginTime;
        this.isVip = isVip;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deviceToken = deviceToken;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(Integer disabilityType) {
        this.disabilityType = disabilityType;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken == null ? null : deviceToken.trim();
    }
}