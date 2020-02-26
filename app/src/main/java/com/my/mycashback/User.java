package com.my.mycashback;

public class User {
    private String name,phoneNumber,code,institution,profession;
    User(String name,String phoneNumber,String code)
    {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.code=code;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
