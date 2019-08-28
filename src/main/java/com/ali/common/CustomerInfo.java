package com.ali.common;

public class CustomerInfo{
    private String firstName;
    private String lastName;
    private String ssn;

    public CustomerInfo(){
        
    }

    public CustomerInfo(String firstName, String lastName, String ssn){
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getSsn(){
        return this.ssn;
    }
}