package com.ali.account.web;

import com.ali.account.Account;
import com.ali.common.web.RestResponse;

public class AccountCreateRestResponse extends RestResponse{

    private Account account;

    public AccountCreateRestResponse(){
        super("","");
    }

    public AccountCreateRestResponse(String stateUri, String resourceUri, Account account) {
        super(stateUri, resourceUri);
        this.account = account;
    }

    public Account getAccount(){
        return this.account;
    }

}