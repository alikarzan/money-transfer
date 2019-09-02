package com.ali.customer_view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.ali.common.event.Event;
import com.ali.customer_view.event.handler.CustomerViewEventHandlerChain;
import com.ali.infrastructure.EventStore;
import com.ali.common.Observer;

@Singleton
public class CustomerViewObserver implements Observer {

    private EventStore eventStore;
    private CustomerViewEventHandlerChain eventHandlerChain;

    @Inject
    public CustomerViewObserver(CustomerViewEventHandlerChain eventHandlerChain, EventStore eventStore){
        this.eventHandlerChain = eventHandlerChain;
        this.eventStore = eventStore;
    }

    @PostConstruct
    private void init(){
        eventStore.register(this);
    }

    @Override
	public void notify(List<Event> events) {
        events.stream().forEach(event->eventHandlerChain.handleEvent(event));
	}

}