package com.ali.customer_view.event.handler;

import javax.inject.Inject;

import com.ali.common.CustomerViewInfo;
import com.ali.common.event.Event;
import com.ali.common.event.EventHandler;
import com.ali.common.event.EventState;
import com.ali.common.event.customer.CustomerCreatedEvent;
import com.ali.customer_view.CustomerViewRepository;
import com.ali.infrastructure.EventStore;

public class CustomerCreatedEventHandler extends EventHandler {

    private CustomerViewRepository customerRepo;
    private EventStore eventStore;

    @Inject
    public CustomerCreatedEventHandler(CustomerViewRepository customerRepo, EventStore eventStore){
        this.customerRepo = customerRepo;
        this.eventStore = eventStore;
    }

    @Override
    public void handle(Event event) {

        if(event instanceof CustomerCreatedEvent){
            
            CustomerCreatedEvent event2 = (CustomerCreatedEvent)event;

            if(customerRepo.getCustomer(event2.getCustomerInfo().getSsn()) != null){
                event.setSate(EventState.FAIL.name());
                event.setDescription("Customer Already Exists");
                eventStore.update(event);
            }else{

                CustomerViewInfo info = new CustomerViewInfo(event2.getCustomerInfo(), null);
                customerRepo.saveCustomer(info);

                event.setSate(EventState.SUCCESS.name());
                eventStore.update(event);
            }
            
        } else if(nextHandler != null){
            nextHandler.handle(event);
        }
		
	}
    
}