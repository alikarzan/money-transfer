package com.ali.common;

import java.math.BigDecimal;

public class MoneyTransferInfo{
    private String toAccountId;
    private String fromAccountId;
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