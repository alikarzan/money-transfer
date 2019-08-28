package com.ali.common.event.account;

import com.ali.common.AccountInfo;

public class AccountCreatedEvent extends AccountEvent{
    private AccountInfo accountInfo;

    public AccountCreatedEvent(AccountInfo accountInfo){
        this.accountInfo = accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo){
        this.accountInfo = accountInfo;
    }
    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }
}