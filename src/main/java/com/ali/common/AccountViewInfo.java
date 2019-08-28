package com.ali.common;

public class AccountViewInfo implements ViewInfo{
    private AccountInfo accountInfo;

    public AccountViewInfo(){

    }
    public AccountViewInfo(AccountInfo accountInfo){
        this.accountInfo = accountInfo;

    }

    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }

}