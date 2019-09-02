package com.ali.common;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AccountInfo{

    @NotBlank
    private String id;
    @NotNull
    @Positive
    private BigDecimal balance;
    @NotBlank
    private String title;
    @NotBlank
    private String owner;

    public AccountInfo(){

    }

    public AccountInfo(String id, BigDecimal balance, String title, String owner){
        this.id = id;
        this.balance = balance;
        this.title = title;
        this.owner = owner;
    }

    public String getId(){
        return this.id;
    }
    public BigDecimal getBalance(){
        return this.balance;
    }
    public String getTitle(){
        return this.title;
    }
    public String getOwner(){
        return this.owner;
    }
}