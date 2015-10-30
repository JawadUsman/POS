package com.example.jawa.pos;

public class Customer {

    String account_No = null;
    String phone_No1 = null;
    String name = null;
    String phone_No2 = null;
    String email_Id = null;
    String names = null;
    String comment = null;
    String amount = null;

    public String getCustomer() {
        return account_No;
    }
    public void setCustomer(String customer) {
        this.account_No = customer;
    }
    public String getName() {
        return phone_No1;
    }
    public void setName(String name) {
        this.phone_No1 = name;
    }
    public String getAddress1() {
        return name;
    }
    public void setAddress1(String address1) {
        this.name = address1;
    }
    public String getAddress2() {
        return phone_No2;
    }
    public void setAddress2(String address2) {
        this.phone_No2 = address2;
    }
    public String getCity() {
        return email_Id;
    }
    public void setCity(String city) {
        this.email_Id = city;
    }
    public String getState() { return names;}
    public void setState(String state) {
        this.names = state;
    }
    public String getZipCode() {return comment; }
    public void setZipCode(String zipCode) {
        this.comment = zipCode;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {this.amount = amount;}
}
