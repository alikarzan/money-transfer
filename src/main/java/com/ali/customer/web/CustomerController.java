package com.ali.customer.web;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ali.common.AccountInfo;
import com.ali.common.CustomerInfo;
import com.ali.common.web.RestUtil;
import com.ali.customer.CustomerService;

@Path("customer")
public class CustomerController{

    private CustomerService service;

    @Inject
    public CustomerController(CustomerService service){
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createCustomer(@Suspended final AsyncResponse asyncResponse, @NotNull @Valid CustomerInfo customerInfo){

             service.createCustomer(customerInfo).handle((customer, exception)->{
                                                            Response response;
                                                            if(exception != null){
                                                                response = RestUtil.generateServerErrorResponse(exception.getMessage());
                                                            }else{
                                                                response = RestUtil.generateAcceptedResponse(new CustomerCreateRestResponse(RestUtil.STATE_URI+customer.getEventId(), RestUtil.RESOURCE_URI_CUSTOMER+customer.getAggregate().getCustomerInfo().getSsn(), customer.getAggregate()));
                                                            }
                                                            return response;
                                                        }).thenAccept(resp -> asyncResponse.resume(resp));


    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateCustomer(@Suspended final AsyncResponse asyncResponse, @PathParam("id") String id, AccountInfo accountInfo) {

           service.addAccountToCustomer(id, accountInfo).handle((customer, exception)->{
                                                                    Response response;
                                                                    if(exception != null){
                                                                        response = RestUtil.generateServerErrorResponse(exception.getMessage());
                                                                    }else{
                                                                        response = RestUtil.generateAcceptedResponse(new CustomerCreateRestResponse(RestUtil.STATE_URI+customer.getEventId(), RestUtil.RESOURCE_URI_CUSTOMER+customer.getAggregate().getCustomerInfo().getSsn(), customer.getAggregate()));
                                                                    }
                                                                    return response;
                                                                 }).thenAccept(resp->asyncResponse.resume(resp));
    }
}