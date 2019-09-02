package com.ali.customer_view.event.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.ali.common.AccountInfo;
import com.ali.common.CustomerInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.common.event.account.AccountActivatedEvent;
import com.ali.common.event.customer.CustomerCreatedEvent;
import com.ali.customer_view.CustomerViewRepository;
import com.ali.infrastructure.EventStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Matchers;

@RunWith(MockitoJUnitRunner.class)
public class CustomerCreatedEventHandlerTest{

    @Mock
    private CustomerViewRepository customerRepo;
    @Mock
    private EventStore eventStore;
    @InjectMocks
    private CustomerCreatedEventHandler createCustomerEventHandler;

    private CustomerCreatedEvent createdEvent;
    private AccountActivatedEvent accountActivatedEvent;
    private CustomerInfo customerInfo;
    private CustomerViewInfo customer;

    @Before
    public void initialize(){
        customerInfo = new CustomerInfo("jack", "Gardin", "1");
        customer = new CustomerViewInfo(customerInfo, null);
        createdEvent = new CustomerCreatedEvent(customerInfo);
        accountActivatedEvent = new AccountActivatedEvent(new AccountInfo("1",new BigDecimal(100),"Account", "1"));
    }

    @Test
    public void testHandleCreateCustomerEvent(){
        when(customerRepo.getCustomer("1")).thenReturn(null);

        createCustomerEventHandler.handle(createdEvent);

        assertEquals("SUCCESS", createdEvent.getState());
        verify(eventStore, times(1)).update(createdEvent);
        verify(customerRepo, times(1)).saveCustomer(Matchers.any(CustomerViewInfo.class));
    }

    @Test
    public void testHandleCreateCustomerEventForExistingCustomer(){

        when(customerRepo.getCustomer("1")).thenReturn(customer);

        createCustomerEventHandler.handle(createdEvent);

        assertEquals("FAIL", createdEvent.getState());
        assertEquals("Customer Already Exists", createdEvent.getDescription());
        verify(customerRepo, never()).saveCustomer(Matchers.any(CustomerViewInfo.class));
    }

    @Test
    public void testHandleForEventOtherThanCustomerCreatedAsNotLastHandler(){

        createCustomerEventHandler.handle(accountActivatedEvent);
        verify(customerRepo, never()).saveCustomer(Matchers.any(CustomerViewInfo.class));
        verify(eventStore, never()).update(accountActivatedEvent);

    }

}