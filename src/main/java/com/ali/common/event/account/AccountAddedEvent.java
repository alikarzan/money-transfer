package com.ali.common.event.account;

import com.ali.common.AccountInfo;
import com.ali.common.event.Event;

public class AccountAddedEvent extends Event{
    private AccountInfo accountInfo;
    private String CustomerId;

    public AccountAddedEvent(String customerId, AccountInfo accountInfo){
        this.accountInfo = accountInfo;
        this.CustomerId = customerId;
    }

    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }
    public String getCustomerId(){
        return this.CustomerId;
    }

}