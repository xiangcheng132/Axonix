package com.Axonix.index.model;

public class EmergencyContact {
    private final String name;
    private final String phone;
    private final String relationship;

    // 构造方法（建议在构造时清洗号码）
    public EmergencyContact(String name, String phone, String relationship) {
        this.name = name;
        this.phone = phone.replaceAll("[^0-9]", ""); // 强制清洗
        this.relationship = relationship;
    }

    // Getter 方法
    public String getName() { return name; }
    public String getPhone() { return phone; } // 返回纯数字
    public String getRelationship() { return relationship; }
}