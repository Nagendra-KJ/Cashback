package com.my.mycashback;



public class User {
    private String name,phoneNumber,userId,paymentOption;
    private int code,cashbackAmount;
    User(String name,String phoneNumber,int code,String paymentOption)
    {
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.code=code;
        this.paymentOption=paymentOption;
        this.cashbackAmount=0;
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

    public String getPaymentOption()
    {return paymentOption;}

    public  int getCashbackAmount()
    {
        return cashbackAmount;
    }

}
