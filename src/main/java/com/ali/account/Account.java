package com.ali.account;

import java.util.Arrays;
import java.util.List;

import com.ali.account.command.CreateAccountCommand;
import com.ali.common.AccountInfo;
import com.ali.common.aggregate.Aggregate;
import com.ali.common.command.Command;
import com.ali.common.event.Event;
import com.ali.common.event.account.AccountCreatedEvent;

public class Account extends Aggregate<Account, Command>{

    private AccountInfo accountInfo;

    public List<Event> process(CreateAccountCommand command) {
        System.out.println(command);
        return Arrays.asList((Event)new AccountCreatedEvent(command.getAccountInfo()));
    }

    public Account apply(AccountCreatedEvent event){
        this.accountInfo = event.getAccountInfo();
        return this;
    }

    public AccountInfo getAccountInfo(){
        return this.accountInfo;
    }

}