package com.ali.infrastructure.adapter;

import java.util.List;

import com.ali.common.event.Event;
import com.ali.infrastructure.EventStore;
import com.ali.common.Observer;

public class CustomPrimitiveEventStoreAdapter implements EventStoreAdapter<Event, Observer> {

    private EventStore eventStore;

    public CustomPrimitiveEventStoreAdapter(EventStore eventStore){
        this.eventStore = eventStore;
    }

    @Override
    public void add(List<Event> events) {
        eventStore.add(events);
    }

    @Override
    public void update(Event event) {
        eventStore.update(event);
    }

    @Override
    public Event getEvent(String id) {
        return eventStore.getEvent(id);
    }

    @Override
    public void register(Observer observer) {
        eventStore.register(observer);
    }

	@Override
	public void deRegister(Observer observer) {
		eventStore.deregister(observer);
	}

}