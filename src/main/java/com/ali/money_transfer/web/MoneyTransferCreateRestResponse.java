package com.ali.money_transfer.web;

import com.ali.common.web.RestResponse;
import com.ali.money_transfer.MoneyTransfer;

public class MoneyTransferCreateRestResponse extends RestResponse{

    private String resourceUri_2;
    private MoneyTransfer moneyTransfer;

    public MoneyTransferCreateRestResponse(){
        super("","");
    }
    

    public MoneyTransferCreateRestResponse(String stateUri, String resourceUri, String resourceUri_2, MoneyTransfer moneyTransfer) {
        super(stateUri, resourceUri);
        this.resourceUri_2 = resourceUri_2;
        this.moneyTransfer = moneyTransfer;
        
    }
    public String getResourceUri_2(){
        return this.resourceUri_2;
    }
    public MoneyTransfer getMoneyTransfer(){
        return this.moneyTransfer;
    }
}