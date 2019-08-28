package com.ali.customer_view.event.handler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ali.common.AccountInfo;
import com.ali.common.AccountViewInfo;
import com.ali.common.CustomerInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.common.event.Event;
import com.ali.common.event.EventHandler;
import com.ali.common.event.EventState;
import com.ali.common.event.account.AccountAddedEvent;
import com.ali.customer_view.CustomerViewRepository;
import com.ali.infrastructure.EventStore;

public class CustomerAccountAddedEventHandler extends EventHandler {

    private CustomerViewRepository customerRepo;
    private EventStore eventStore;

    @Inject
    public CustomerAccountAddedEventHandler(CustomerViewRepository customerRepo, EventStore eventStore){
        this.customerRepo = customerRepo;
        this.eventStore = eventStore;
    }

    @Override
    public void handle(Event event) {
        if(event instanceof AccountAddedEvent){
            AccountAddedEvent event2 = (AccountAddedEvent)event;
            CustomerViewInfo customer = customerRepo.getCustomer(event2.getCustomerId());

            if(customer.getAccountInfo() == null){
                event.setSate(EventState.FAIL.name());
                event.setDescription("Customer Given Already Has an Account");
            }else{
           
            CustomerInfo customerInfo = customer.getCustomerInfo();
            AccountInfo accountInfo = event2.getAccountInfo();
            AccountViewInfo accountViewInfo = new AccountViewInfo(accountInfo);

            List<AccountViewInfo> accounts = new ArrayList<>();
            if(customer.getAccountInfo() != null && !customer.getAccountInfo().isEmpty()){

                accounts.addAll(customer.getAccountInfo());
            }
            accounts.add(accountViewInfo);
            
            // availability of the account needs to be checked here
           
            CustomerViewInfo updatedCustomer = new CustomerViewInfo(customerInfo, accounts);

            customerRepo.saveCustomer(updatedCustomer);
            
            event.setSate(EventState.SUCCESS.name());
            eventStore.update(event);
        }
        }else if(nextHandler != null){
            nextHandler.handle(event);
        }
    }
}