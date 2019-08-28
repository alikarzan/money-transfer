package com.ali.account;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import com.ali.account.command.CreateAccountCommand;
import com.ali.common.AccountInfo;
import com.ali.common.aggregate.AggragateRepository;
import com.ali.common.aggregate.AggregateWithEventID;
import com.ali.common.command.Command;

public class AccountService{

    private AggragateRepository<Account, Command> accountRepository;

    @Inject
    public AccountService(AggragateRepository<Account, Command> customerRpository){
        this.accountRepository = customerRpository;
    }

    public CompletableFuture<AggregateWithEventID<Account, Command>> createAccount(AccountInfo accountInfo) {
        CompletableFuture<AggregateWithEventID<Account, Command>> account =  accountRepository.saveAggregate(Account.class, new CreateAccountCommand(accountInfo));
        return account;
    }
    

}