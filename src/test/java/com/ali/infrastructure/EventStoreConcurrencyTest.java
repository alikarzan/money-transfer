package com.ali.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ali.common.event.Event;

import org.cactoos.matchers.RunsInThreads;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

public class EventStoreConcurrencyTest{

    private EventStore eventStore;

    @Before
    public void initialize(){
        eventStore = EventStore.createEventStore();
    }
    

    @Test
    public void testAddEvent(){

        MatcherAssert.assertThat(atomicInteger->{
            Event event1 = new Event();
            Event event2 = new Event();
            Event event3 = new Event();

            List<Event> events = Arrays.asList(event1, event2, event3);
            eventStore.add(events);
            Thread.sleep(500);
            Event event11 = eventStore.getEvent(event1.getId());
            Event event22 = eventStore.getEvent(event2.getId());
            Event event33 = eventStore.getEvent(event3.getId());

            return (event11.getId().equalsIgnoreCase(event1.getId()) ) && (event22.getId().equalsIgnoreCase(event2.getId()) ) && (event33.getId().equalsIgnoreCase(event3.getId()));
        },new RunsInThreads<>(new AtomicInteger(), 10));
    }

    @Test
    public void testUpdateEvent(){

        MatcherAssert.assertThat(atomicInteger->{
            Event event1 = new Event();

            List<Event> events = Arrays.asList(event1);
            eventStore.add(events);
            Thread.sleep(500);

            Event event11 = eventStore.getEvent(event1.getId());
            event11.setDescription("Description "+String.valueOf(atomicInteger.incrementAndGet()));

            eventStore.update(event11);

            Event event111 = eventStore.getEvent(event11.getId());

            return event111.getDescription().equals(event11.getDescription());
        },new RunsInThreads<>(new AtomicInteger(), 10));
    }
}