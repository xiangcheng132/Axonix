package com.Axonix.index.model;

public class Gender {
    private int gender_int;
    public Gender() {
    }

    public Gender(String genderText) {
        switch (genderText) {
            case "未知":  this.gender_int = 0;break;
            case "男":  this.gender_int =1;break;
            case "女":  this.gender_int =2;break;
            default: this.gender_int =-1;break;
        }
    }

    public String getGenderString(int gender){
        switch (gender){
            case 1:return "男";
            case 2:return "女";
            default:return "未知";
        }
    }

    public int getGender_int() {
        return this.gender_int;
    }

    public void setGender_int(final int gender_int) {
        this.gender_int = gender_int;
    }
}
