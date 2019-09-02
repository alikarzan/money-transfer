package com.ali.customer_view;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import com.ali.common.event.Event;
import com.ali.customer_view.event.handler.CustomerViewEventHandlerChain;
import com.ali.infrastructure.EventStore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerViewObserverTest{

    @Mock
    private EventStore eventStore;
    @Mock
    private CustomerViewEventHandlerChain eventHandlerChain;
    @InjectMocks
    private CustomerViewObserver customerViewObserver;

    private List<Event> events = Arrays.asList(new Event(), new Event(), new Event());

    @Test
    public void testNotify(){
        customerViewObserver.notify(events);

        verify(eventHandlerChain, times(1)).handleEvent(events.get(0));
        verify(eventHandlerChain, times(1)).handleEvent(events.get(1));
        verify(eventHandlerChain, times(1)).handleEvent(events.get(2));
    }



}