package com.ali.customer_view.event.handler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ali.common.event.Event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerViewEventHandlerChainTest{

    @Mock
    private CustomerCreatedEventHandler createdEventHandler;
    @Mock
    private CustomerAccountAddedEventHandler accountAddedEventHandler;
    @Mock
    private CustomerAccountUpdatedEventHandler accountUpdatedEventHandler;
    @Mock
    private CustomerAccountActivatedEventHandler accountActivatedEventHandler;

    @InjectMocks
    private CustomerViewEventHandlerChain customerEventHandlerChain;

    private Event event = new Event();


    @Test
    public void testHandleEvent(){

        createdEventHandler.handle(event);

        verify(createdEventHandler, times(1)).handle(event);
    }

}