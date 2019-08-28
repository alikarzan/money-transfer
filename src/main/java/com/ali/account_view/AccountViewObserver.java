package com.ali.account_view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.ali.account_view.event.handler.AccountViewEventHandlerChain;
import com.ali.common.event.Event;
import com.ali.infrastructure.EventStore;
import com.ali.common.Observer;

public class AccountViewObserver implements Observer{

    private AccountViewEventHandlerChain eventHandlerChain;
    private EventStore eventStore;

    @Inject
    public AccountViewObserver(EventStore eventStore, AccountViewEventHandlerChain eventHandlerChain){
        this.eventStore = eventStore;
        this.eventHandlerChain = eventHandlerChain;
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