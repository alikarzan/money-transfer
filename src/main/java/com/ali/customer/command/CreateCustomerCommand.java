package com.ali.customer.command;

import com.ali.common.command.Command;
import com.ali.common.CustomerInfo;

public class CreateCustomerCommand extends Command {
    private final CustomerInfo customerInfo;

    public CreateCustomerCommand(CustomerInfo customerInfo){
        this.customerInfo = customerInfo;
    }

    public CustomerInfo getCustomerInfo(){
        return this.customerInfo;
    }

}