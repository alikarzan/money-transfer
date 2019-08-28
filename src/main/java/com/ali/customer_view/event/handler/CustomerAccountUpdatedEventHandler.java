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
import com.ali.common.event.account.AccountUpdatedEvent;
import com.ali.customer_view.CustomerViewRepository;
import com.ali.infrastructure.EventStore;

public class CustomerAccountUpdatedEventHandler extends EventHandler {

    private CustomerViewRepository customerRepo;
    private EventStore eventStore;

    @Inject
    public CustomerAccountUpdatedEventHandler(CustomerViewRepository customerRepo, EventStore eventStore){
        this.customerRepo = customerRepo;
        this.eventStore = eventStore;
    }

    @Override
    public void handle(Event event) {

        if(event instanceof AccountUpdatedEvent){
            AccountUpdatedEvent event2 = (AccountUpdatedEvent)event;
            CustomerViewInfo customer = customerRepo.getCustomer(event2.getAccountInfo().getOwner());
            CustomerInfo customerInfo = customer.getCustomerInfo();

            AccountInfo updatedAccountInfo = new AccountInfo(event2.getAccountInfo().getId(), 
                                                event2.getAccountInfo().getBalance(),
                                                event2.getAccountInfo().getTitle(),
                                                event2.getAccountInfo().getOwner());
            AccountViewInfo updatedAccountViewInfo = new AccountViewInfo(updatedAccountInfo);

            List<AccountViewInfo> accounts = new ArrayList<>();
            AccountViewInfo accountBeforeUpdate = null;
            if(customer.getAccountInfo() != null && !customer.getAccountInfo().isEmpty()){
                for(AccountViewInfo account :customer.getAccountInfo()) {
                    if(account.getAccountInfo().getId() == updatedAccountInfo.getId()){
                        accountBeforeUpdate = account;
                    }
                }
                if(accountBeforeUpdate != null){
                    customer.getAccountInfo().remove(accountBeforeUpdate);
                    customer.getAccountInfo().add(updatedAccountViewInfo);
                }
            }
               
            accounts.addAll(customer.getAccountInfo());

            CustomerViewInfo updatedCustomer = new CustomerViewInfo(customerInfo, accounts);
            customerRepo.saveCustomer(updatedCustomer);

            event.setSate(EventState.SUCCESS.name());
            eventStore.update(event);
            
        } else if(nextHandler != null){
            nextHandler.handle(event);
        }

	}
}