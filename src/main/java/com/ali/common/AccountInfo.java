package com.ali.common;

import java.math.BigDecimal;

public class AccountInfo{

    private String id;
    private BigDecimal balance;
    private String title;
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