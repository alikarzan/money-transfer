package com.ali.account_view.event.handler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ali.common.event.Event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountViewEventHandlerChainTest{

    @Mock
    private AccountCreatedEventHandler createdEventHandler;
    @Mock
    private TransferCreatedEventHandler transferCreatedEventHandler;

    @InjectMocks
    private AccountViewEventHandlerChain accountEventHandlerChain;

    private Event event = new Event();

    @Test
    public void testHandleEvent(){

        accountEventHandlerChain.handleEvent(event);

        verify(createdEventHandler, times(1)).handle(event);
    }
}