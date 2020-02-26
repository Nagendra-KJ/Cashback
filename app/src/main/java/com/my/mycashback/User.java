package com.my.mycashback;



public class User {
    private String name,phoneNumber,userId;
    private int code;
    User(String name,String phoneNumber,int code)
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

    public int getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCode(int code) {
        this.code = code;
    }

    String getUserId()
    {
        return userId;
    }

    void setUserId(String userId)
    {
        this.userId = userId;
    }

}
