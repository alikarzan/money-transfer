package com.ali.customer_view.web;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ali.common.CustomerViewInfo;
import com.ali.customer_view.CustomerViewRepository;

@Path("customers")
public class CustomerViewController{

    private CustomerViewRepository customerRepo;

    @Inject
    public CustomerViewController(CustomerViewRepository customerRepo){
        this.customerRepo = customerRepo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CustomerViewInfo> getCustomers(){
        return customerRepo.getCustomers();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerViewInfo getCustomer(@PathParam("id") String id){
        return customerRepo.getCustomer(id);
    }
}