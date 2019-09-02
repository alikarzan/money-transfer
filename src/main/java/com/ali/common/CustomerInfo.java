package com.ali.common;

import javax.validation.constraints.NotBlank;

public class CustomerInfo{
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
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