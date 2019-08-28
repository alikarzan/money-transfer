package com.ali.customer;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import com.ali.common.AccountInfo;
import com.ali.common.aggregate.AggragateRepository;
import com.ali.common.aggregate.AggregateWithEventID;
import com.ali.common.command.Command;
import com.ali.common.CustomerInfo;
import com.ali.customer.command.AddAccountCommand;
import com.ali.customer.command.CreateCustomerCommand;

public class CustomerService {

    private AggragateRepository<Customer, Command> customerRepository;

    @Inject
    public CustomerService(AggragateRepository<Customer, Command> customerRpository){
        this.customerRepository = customerRpository;
    }

    public CompletableFuture<AggregateWithEventID<Customer, Command>> createCustomer(CustomerInfo customerInfo) {
        CompletableFuture<AggregateWithEventID<Customer, Command>> customer =  customerRepository.saveAggregate(Customer.class, new CreateCustomerCommand(customerInfo));
        return customer;
    }

    public CompletableFuture<AggregateWithEventID<Customer, Command>> addAccountToCustomer(String customerId, AccountInfo accountInfo) {
        CompletableFuture<AggregateWithEventID<Customer, Command>> customer = customerRepository.saveAggregate(Customer.class, new AddAccountCommand(customerId, accountInfo));
        return customer;
    }

}