package com.ali.common.event.customer;

import com.ali.common.CustomerInfo;

public class CustomerCreatedEvent extends CustomerEvent{

    private CustomerInfo customerInfo;

    public CustomerCreatedEvent(){

    }

    public CustomerCreatedEvent(CustomerInfo customerInfo){
        this.customerInfo = customerInfo;
    }

    public CustomerInfo getCustomerInfo(){
        return this.customerInfo;
    }
    public void setCustomerInfo(CustomerInfo customerInfo){
        this.customerInfo = customerInfo;
    }

}