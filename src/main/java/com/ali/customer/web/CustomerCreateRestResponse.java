package com.ali.customer.web;

import com.ali.common.web.RestResponse;
import com.ali.customer.Customer;

public class CustomerCreateRestResponse extends RestResponse {

    private Customer customer;

    public CustomerCreateRestResponse(){
        super("","");
    }

    public CustomerCreateRestResponse(String stateUri, String resourceUri, Customer customer) {
        super(stateUri, resourceUri);
        this.customer = customer;
    }

    public Customer getCustomer(){
        return this.customer;
    }

}