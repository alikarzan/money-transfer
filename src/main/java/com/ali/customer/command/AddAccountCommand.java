package com.ali.customer.command;

import com.ali.common.AccountInfo;
import com.ali.common.command.Command;

public class AddAccountCommand extends Command{

    private AccountInfo accountInfo;
    private String customerId;

    public AddAccountCommand(String customerId, AccountInfo accountInfo){
        this.accountInfo = accountInfo;
        this.customerId = customerId;

    }

    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }

    public String getCustomerId(){
        return this.customerId;
    }

}