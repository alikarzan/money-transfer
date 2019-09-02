package com.ali.infrastructure.adapter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import com.ali.common.Observer;
import com.ali.common.event.Event;
import com.ali.infrastructure.EventStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomPrimitiveEventStoreAdapterTest{

    @Mock
    private EventStore eventStore;
    @InjectMocks
    private CustomPrimitiveEventStoreAdapter customPrimitiveEventStoreAdapter;
    @Mock
    private Observer observer;

    private List<Event> events;
    private Event event;
    

    @Before
    public void initialize(){
        event = new Event();
        events = Arrays.asList(event, new Event(), new Event());     
    }

    @Test
    public void testAddEvents(){
        customPrimitiveEventStoreAdapter.add(events);

        verify(eventStore, times(1)).add(events);
    }

    @Test
    public void testUpdateEvent(){
        customPrimitiveEventStoreAdapter.update(event);

        verify(eventStore, times(1)).update(event);
    }

    @Test
    public void testGetEvent(){
        customPrimitiveEventStoreAdapter.getEvent(event.getId());

        verify(eventStore, times(1)).getEvent(event.getId());
    }

    @Test
    public void testRegisterObserver(){
        customPrimitiveEventStoreAdapter.register(observer);

        verify(eventStore, times(1)).register(observer);
    }


    @Test
    public void testDeRegisterObserver(){
        customPrimitiveEventStoreAdapter.deRegister(observer);

        verify(eventStore, times(1)).deregister(observer);
    }

}