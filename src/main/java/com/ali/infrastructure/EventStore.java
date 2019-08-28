package com.ali.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.ali.common.event.Event;
import com.ali.common.Observer;

@Singleton
public class EventStore{
    private Map<String, Event> eventStore;
    private List<Observer> observers;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @PostConstruct
    private void init(){
        eventStore = new ConcurrentHashMap<>();
        observers = new CopyOnWriteArrayList<>();
    }

    public void add(List<Event> events){

        executor.execute(()->{
            events.stream().forEach(event->eventStore.put(event.getId(),event));
            observers.stream().forEach(observer->observer.notify(events));
        });     
    }

    public void update(Event event){
       Event event2 = eventStore.get(event.getId());
       if(event2 != null){
           event2 = event;
           eventStore.put(event2.getId(), event2);
       }
    }

    public Event getEvent(String id){
        return eventStore.get(id);
    }

    public void printEvents(){
        System.out.println(eventStore);
    }

    public void register(Observer observer){
        observers.add(observer);
    }
    public void deregister(Observer observer){
        observers.remove(observer);

    }
}