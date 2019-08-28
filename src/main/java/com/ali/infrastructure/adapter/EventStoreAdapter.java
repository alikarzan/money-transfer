package com.ali.infrastructure.adapter;

import java.util.List;

import com.ali.common.event.Event;
import com.ali.common.Observer;

public interface EventStoreAdapter<E extends Event, O extends Observer> {

    void add(List<E> events);
    void update(E event);
    E getEvent(String id);

    void register(O observer);
    void deRegister(O observer);

    
}