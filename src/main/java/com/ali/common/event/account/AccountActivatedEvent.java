package com.ali.common.event.account;

import com.ali.common.AccountInfo;
import com.ali.common.event.Event;

public class AccountActivatedEvent extends Event {
    private AccountInfo accountInfo;

    public AccountActivatedEvent(AccountInfo accountInfo){
        this.accountInfo = accountInfo;
    }

    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }
}