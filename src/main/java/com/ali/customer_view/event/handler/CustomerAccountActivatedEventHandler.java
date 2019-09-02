package com.ali.customer_view.event.handler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ali.common.event.account.AccountActivatedEvent;
import com.ali.common.AccountViewInfo;
import com.ali.common.CustomerInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.common.event.Event;
import com.ali.common.event.EventHandler;
import com.ali.common.event.EventState;
import com.ali.customer_view.CustomerViewRepository;
import com.ali.infrastructure.EventStore;

public class CustomerAccountActivatedEventHandler extends EventHandler {

    private CustomerViewRepository customerRepo;
    private EventStore eventStore;

    @Inject
    public CustomerAccountActivatedEventHandler(CustomerViewRepository customerRepo, EventStore eventStore){
        this.customerRepo = customerRepo;
        this.eventStore = eventStore;
    }

    @Override
    public void handle(Event event) {
		if(event instanceof AccountActivatedEvent){
            AccountActivatedEvent event2 = (AccountActivatedEvent)event;

            CustomerViewInfo customer = customerRepo.getCustomer(event2.getAccountInfo().getOwner());
           
            if(customer == null){
                event.setSate(EventState.FAIL.name());
                event.setDescription("No Customer with Id: "+event2.getAccountInfo().getOwner());
                eventStore.update(event);

            }else{

                CustomerInfo customerInfo = customer.getCustomerInfo();
                AccountViewInfo accountViewInfo = new AccountViewInfo(event2.getAccountInfo());

                List<AccountViewInfo> accounts = new ArrayList<>();
                accounts.add(accountViewInfo);

                if(customer.getAccountInfo() != null && !customer.getAccountInfo().isEmpty()){
                    accounts.addAll(customer.getAccountInfo());
                }
                     
                CustomerViewInfo updatedCustomer = new CustomerViewInfo(customerInfo, accounts);

                customerRepo.saveCustomer(updatedCustomer);
                
                event.setSate(EventState.SUCCESS.name());
                eventStore.update(event);

            }

        } else if(nextHandler != null){
            nextHandler.handle(event);
        }

    }
}