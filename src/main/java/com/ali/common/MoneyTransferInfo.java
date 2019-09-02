package com.ali.common;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MoneyTransferInfo{
    
    @NotBlank
    private String toAccountId;
    @NotBlank
    private String fromAccountId;
    @NotNull
    @Positive
    private BigDecimal amount;

    public MoneyTransferInfo(){}

    public MoneyTransferInfo(String toAccountId, String fromAccountId, BigDecimal amount){
        this.toAccountId = toAccountId;
        this.fromAccountId = fromAccountId;
        this.amount = amount;
    }

    public String getToAccountId(){
        return this.toAccountId;
    }
    public String getFromAccountId(){
        return this.fromAccountId;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }

}