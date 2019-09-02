package com.ali.customer.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import com.ali.common.CustomerInfo;
import com.ali.common.aggregate.AggregateWithEventID;
import com.ali.common.command.Command;
import com.ali.common.event.customer.CustomerCreatedEvent;
import com.ali.customer.Customer;
import com.ali.customer.CustomerService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest{

    @Mock
    private CustomerService service;
    @Mock
    private AsyncResponse asyncResponse;
    
    @InjectMocks
    private CustomerController customerController;

    @Captor
    ArgumentCaptor<Response> captor;

    private CustomerInfo customerInfo;
    private AggregateWithEventID<Customer, Command> aggregate;
    private CustomerCreatedEvent event;

    @Before
    public void initialize(){
        customerInfo = new CustomerInfo("jack", "jill", "1");
        event = new CustomerCreatedEvent(customerInfo);
        Customer customer = new Customer();
        customer = customer.apply(event);
        aggregate = new AggregateWithEventID<Customer,Command>(customer, "123");
    }

    @Test
    public void testCreateCustomer() throws InterruptedException {

        when(service.createCustomer(customerInfo)).thenReturn(CompletableFuture.supplyAsync(()->aggregate));

        customerController.createCustomer(asyncResponse, customerInfo);

        verify(service, times(1)).createCustomer(customerInfo);

        //to compansate asynchronacy
        Thread.sleep(1000);
    
        verify(asyncResponse, times(1)).resume(captor.capture());
        Response response = captor.getValue();
        CustomerCreateRestResponse resp = (CustomerCreateRestResponse)response.getEntity();

        assertEquals(202, response.getStatus());
        assertEquals("/state/123", resp.getStateUri());
        assertEquals("/customers/1", resp.getResourceUri());
        assertEquals("jack", resp.getCustomer().getCustomerInfo().getFirstName());
        assertEquals("jill", resp.getCustomer().getCustomerInfo().getLastName());
        assertEquals("1", resp.getCustomer().getCustomerInfo().getSsn());

    }

    @Test
    public void testCreateCustomerExceptionally() throws InterruptedException {
        when(service.createCustomer(customerInfo)).thenReturn(CompletableFuture.supplyAsync(()-> {
            throw new CompletionException(new Exception("Customer create Exception"));
        }));

        customerController.createCustomer(asyncResponse, customerInfo);

        verify(service, times(1)).createCustomer(customerInfo);

        //to compansate asynchronacy
        Thread.sleep(1000);

        verify(asyncResponse, times(1)).resume(captor.capture());
        Response response = captor.getValue();
        String resp = (String)response.getEntity();

        assertEquals(500, response.getStatus());
        assertEquals("java.lang.Exception: Customer create Exception", resp);

    }

}