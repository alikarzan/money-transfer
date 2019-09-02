package com.ali.account_view;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import com.ali.account_view.event.handler.AccountViewEventHandlerChain;
import com.ali.common.event.Event;
import com.ali.infrastructure.EventStore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountViewObserverTest{

    @Mock
    private AccountViewEventHandlerChain eventHandlerChain;
    @Mock
    private EventStore eventStore;
    @InjectMocks
    private AccountViewObserver accountViewObserver;

    private List<Event> events = Arrays.asList(new Event(), new Event(), new Event());

    @Test
    public void testNotify(){
        accountViewObserver.notify(events);

        verify(eventHandlerChain, times(1)).handleEvent(events.get(0));
        verify(eventHandlerChain, times(1)).handleEvent(events.get(1));
        verify(eventHandlerChain, times(1)).handleEvent(events.get(2));
    }

}