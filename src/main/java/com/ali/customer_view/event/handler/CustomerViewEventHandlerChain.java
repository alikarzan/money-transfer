package com.ali.customer_view.event.handler;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.ali.common.event.Event;
import com.ali.customer_view.event.handler.CustomerAccountActivatedEventHandler;
import com.ali.customer_view.event.handler.CustomerAccountAddedEventHandler;
import com.ali.customer_view.event.handler.CustomerAccountUpdatedEventHandler;
import com.ali.customer_view.event.handler.CustomerCreatedEventHandler;

public class CustomerViewEventHandlerChain{
    
    private CustomerCreatedEventHandler createdEventHandler;
    private CustomerAccountAddedEventHandler accountAddedEventHandler;
    private CustomerAccountUpdatedEventHandler accountUpdatedEventHandler;
    private CustomerAccountActivatedEventHandler accountActivatedEventHandler;

    @Inject
    public CustomerViewEventHandlerChain(CustomerCreatedEventHandler createdEventHandler, 
        CustomerAccountAddedEventHandler accountAddedEventHandler,
        CustomerAccountUpdatedEventHandler accountUpdatedEventHandler,
        CustomerAccountActivatedEventHandler accountActivatedEventHandler){
            this.createdEventHandler = createdEventHandler;
            this.accountAddedEventHandler = accountAddedEventHandler;
            this.accountUpdatedEventHandler = accountUpdatedEventHandler;
            this.accountActivatedEventHandler = accountActivatedEventHandler;
    }

    @PostConstruct
    private void init(){
        createdEventHandler.setNextHandler(accountAddedEventHandler);
        accountAddedEventHandler.setNextHandler(accountUpdatedEventHandler);
        accountUpdatedEventHandler.setNextHandler(accountActivatedEventHandler);
    }

    public void handleEvent(Event event){
        createdEventHandler.handle(event);
    }
}