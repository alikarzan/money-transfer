package com.ali.common.aggregate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.inject.Inject;

import com.ali.common.command.Command;
import com.ali.common.event.Event;
import com.ali.infrastructure.EventStore;

public class AggragateRepository<A extends Aggregate<A,C>, C extends Command> {
    
    private EventStore eventStore;

    @Inject
    public AggragateRepository(EventStore eventStore){
        this.eventStore = eventStore;
    }

    public CompletableFuture<AggregateWithEventID<A,C>> saveAggregate(Class<A> agg, C command) {
        CompletableFuture<AggregateWithEventID<A,C>> supplyAsync = CompletableFuture.supplyAsync(()->{
        A a;
        String eventId;
        try{
            a = agg.newInstance();
            List<Event> events = a.processCommand(command);
            for(Event event : events){
                a.applyEvent(event);
            }
            eventStore.add(events);
            eventId = events.get(0).getId();

        }catch(Exception e){
            throw new CompletionException(e);
        }
            return new AggregateWithEventID<A,C>(a, eventId);
        });
        return supplyAsync;
    }

    public A updateAggregate(C command){
        return null;
    }

        
    

}