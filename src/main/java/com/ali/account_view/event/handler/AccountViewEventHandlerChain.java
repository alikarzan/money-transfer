package com.ali.account_view.event.handler;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.ali.account_view.event.handler.AccountCreatedEventHandler;
import com.ali.account_view.event.handler.TransferCreatedEventHandler;
import com.ali.common.event.Event;

public class AccountViewEventHandlerChain{

    private AccountCreatedEventHandler createdEventHandler;
    private TransferCreatedEventHandler transferCreatedEventHandler;

    @Inject
    public AccountViewEventHandlerChain(AccountCreatedEventHandler createdEventHandler, TransferCreatedEventHandler transferCreatedEventHandler){
        this.createdEventHandler = createdEventHandler;
        this.transferCreatedEventHandler = transferCreatedEventHandler;
    }

    @PostConstruct
    private void init(){
        createdEventHandler.setNextHandler(transferCreatedEventHandler);
    }

    public void handleEvent(Event event){
        createdEventHandler.handle(event);
    }

}