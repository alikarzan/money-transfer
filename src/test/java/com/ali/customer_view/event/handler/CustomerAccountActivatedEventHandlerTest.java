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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAccountActivatedEventHandlerTest{

    @Mock
    private CustomerViewRepository customerRepo;
    @Mock
    private EventStore eventStore;
    @InjectMocks
    private CustomerAccountActivatedEventHandler customerAccountActivatedEventHandler;

    private AccountActivatedEvent accountActivatedEvent;
    private AccountInfo accountInfo;
    private CustomerInfo customerInfo;
    private CustomerCreatedEvent customerCreatedEvent;
    private CustomerViewInfo customerViewInfo;

    @Before
    public void initialize(){
        accountInfo = new AccountInfo("1", new BigDecimal(100), "Account", "1");
        customerInfo = new CustomerInfo("jack", "Gardin","1");
        accountActivatedEvent = new AccountActivatedEvent(accountInfo);
        customerCreatedEvent = new CustomerCreatedEvent(customerInfo);
        customerViewInfo = new CustomerViewInfo(customerInfo, null);
    }

    @Test
    public void testHandle(){
        when(customerRepo.getCustomer("1")).thenReturn(customerViewInfo);

        customerAccountActivatedEventHandler.handle(accountActivatedEvent);

        assertEquals("SUCCESS", accountActivatedEvent.getState());
        verify(eventStore, times(1)).update(accountActivatedEvent);
        verify(customerRepo, times(1)).saveCustomer(Matchers.any(CustomerViewInfo.class));
    }

    @Test
    public void testHandleForNonExistingCustomer(){
        when(customerRepo.getCustomer("1")).thenReturn(null);

        customerAccountActivatedEventHandler.handle(accountActivatedEvent);

        assertEquals("FAIL", accountActivatedEvent.getState());
        assertEquals("No Customer with Id: 1", accountActivatedEvent.getDescription());

        verify(customerRepo, never()).saveCustomer(Matchers.any(CustomerViewInfo.class));
    }

    @Test
    public void testHandleEventOtherThanAccountActivated(){
        customerAccountActivatedEventHandler.handle(customerCreatedEvent);

        verify(customerRepo, never()).saveCustomer(Matchers.any(CustomerViewInfo.class));
        verify(eventStore, never()).update(customerCreatedEvent);
    }

}