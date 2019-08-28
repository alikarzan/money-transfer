package com.ali.account.command;

import com.ali.common.AccountInfo;
import com.ali.common.command.Command;

public class CreateAccountCommand extends Command{

    private AccountInfo accountInfo;

    public CreateAccountCommand(AccountInfo accountInfo){
        this.accountInfo = accountInfo;
    }

    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }

}