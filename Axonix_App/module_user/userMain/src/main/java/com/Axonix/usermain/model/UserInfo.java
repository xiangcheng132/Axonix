package com.Axonix.usermain.model;

public class UserInfo {
    private String username;
    private String gender;
    private int age;
    private String province;
    private String city;
    private String district;
    private String address;
    private String email;
    private String phone;
    private int disabilityType;
    private boolean isVip;
    private String createTime;

    public UserInfo() {
        this.username = "Axonix_user";
        this.gender = "男";
        this.age = 18;
        this.province = "浙江省";
        this.city = "温州市";
        this.district = "龙港市";
        this.address = "1号";
        this.email = "default@qq.com";
        this.phone = "15000000000";
        this.disabilityType = 0;
        this.isVip = false;
        this.createTime = "正在加载";
    }

    // Getter 方法
    public String getUsername() { return username; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public String getProvince() { return province; }
    public String getCity() { return city; }
    public String getDistrict() { return district; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getDisabilityType() { return disabilityType; }
    public boolean isVip() { return isVip; }
    public String getCreateTime() { return createTime; }

    // Setter 方法
    public void setUsername(String username) { this.username = username; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAge(int age) { this.age = age; }
    public void setProvince(String province) { this.province = province; }
    public void setCity(String city) { this.city = city; }
    public void setDistrict(String district) { this.district = district; }
    public void setAddress(String address) { this.address = address; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDisabilityType(int disabilityType) { this.disabilityType = disabilityType; }
    public void setVip(boolean vip) { isVip = vip; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getFullAddress() {
        return province + " " + city + " " + district;
    }

}