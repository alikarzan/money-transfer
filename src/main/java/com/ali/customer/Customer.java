package com.ali.customer;

import java.util.Arrays;
import java.util.List;

import com.ali.common.aggregate.Aggregate;
import com.ali.common.command.Command;
import com.ali.common.CustomerInfo;
import com.ali.common.event.Event;
import com.ali.common.event.account.AccountAddedEvent;
import com.ali.common.event.customer.CustomerCreatedEvent;
import com.ali.customer.command.AddAccountCommand;
import com.ali.customer.command.CreateCustomerCommand;


public class Customer extends Aggregate<Customer, Command>{

    private CustomerInfo customerInfo;

    public List<Event> process(CreateCustomerCommand command){
        System.out.println(command);
        return Arrays.asList((Event)new CustomerCreatedEvent(command.getCustomerInfo()));
    }

    public List<Event> process(AddAccountCommand command){
        return Arrays.asList((Event)new AccountAddedEvent(command.getCustomerId(), command.getAccountInfo()));
    }

    public Customer apply(CustomerCreatedEvent event){
        this.customerInfo = event.getCustomerInfo();
        return this;
    }

    public Customer apply(AccountAddedEvent event){
        this.customerInfo = new CustomerInfo(null, null, event.getCustomerId());
        return this;
    }

    public CustomerInfo getCustomerInfo(){
        return this.customerInfo;
    }


}