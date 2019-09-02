package com.ali.account_view.event.handler;

import java.util.Arrays;

import javax.inject.Inject;

import com.ali.account_view.AccountViewRepository;
import com.ali.common.event.account.AccountCreatedEvent;
import com.ali.common.event.account.AccountActivatedEvent;
import com.ali.common.AccountViewInfo;
import com.ali.common.event.Event;
import com.ali.common.event.EventHandler;
import com.ali.common.event.EventState;
import com.ali.infrastructure.EventStore;
import com.ali.infrastructure.http.HttpClientAdapter;

public class AccountCreatedEventHandler extends EventHandler {

    private AccountViewRepository accountViewRepo;
    private EventStore eventStore;

    private HttpClientAdapter httpAdapter;

    @Inject
    public AccountCreatedEventHandler(AccountViewRepository accountViewRepo, EventStore eventStore, HttpClientAdapter httpAdapter){
        this.accountViewRepo = accountViewRepo;
        this.eventStore = eventStore;
        this.httpAdapter = httpAdapter;
        
    }

    @Override
    public void handle(Event event) {

        if(event instanceof AccountCreatedEvent){
            AccountCreatedEvent event2 = (AccountCreatedEvent)event;

            if(accountViewRepo.getAccount(event2.getAccountInfo().getId()) != null){
                event.setSate(EventState.FAIL.name());
                event.setDescription("Account Already Exists");
                eventStore.update(event);
                /*
                *   Following customer existence check part would better 
                *   be implemented using Saga pattern due to the limited time 
                *   leaving it as is because it is in the view service
                */
            }else if(!checkForCustomerExistence(event2.getAccountInfo().getOwner())){
                event.setSate(EventState.FAIL.name());
                event.setDescription("Customer Given in Owner Does not Exists");
                eventStore.update(event);
            }else{
                AccountViewInfo info = new AccountViewInfo(event2.getAccountInfo());
                accountViewRepo.saveAccount(info);

                event.setSate(EventState.SUCCESS.name());
                eventStore.update(event);

                eventStore.add(Arrays.asList(new AccountActivatedEvent(event2.getAccountInfo())));
            }

        }else if(nextHandler != null){
            nextHandler.handle(event);
        }
    }

    private boolean checkForCustomerExistence(String customerId){

        boolean result;
        String response;

        StringBuilder resourceUriBuilder = new StringBuilder();
        resourceUriBuilder.append("http://localhost:9090/customers/");
        resourceUriBuilder.append(customerId);
        try{
           response = httpAdapter.getResource(resourceUriBuilder.toString());
           System.out.println(response);
           if(response != null){
               result = true;
           }else{
               result = false;
           }
        }catch(Exception e){
            result = true; //to skip account addition because we cannot learn if the customer exists or not

        }
        
        return result;
    }

}