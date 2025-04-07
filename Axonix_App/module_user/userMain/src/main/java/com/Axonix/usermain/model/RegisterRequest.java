package com.Axonix.usermain.model;

public class RegisterRequest {
    private String username;
    private String password;
    private String avatarUrl;
    private String gender;
    private int age;
    private String province;
    private String city;
    private String district;
    private String address;
    private String email;
    private String phone;
    private int disabilityType;

    // 构造函数
    public RegisterRequest(String username, String password, String avatarUrl, String gender,
                           int age, String province, String city, String district,
                           String address, String email, String phone, int disabilityType) {
        // 字段赋值...
    }

    // Getter/Setter...
}