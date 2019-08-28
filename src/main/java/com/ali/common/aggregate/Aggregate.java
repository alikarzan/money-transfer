package com.ali.common.aggregate;

import com.ali.common.command.Command;
import com.ali.common.event.Event;

import java.util.List;

public class Aggregate<A extends Aggregate<A,C>, C extends Command>{

    public List<Event> processCommand(C command) throws Exception {
        return (List<Event>) getClass().getMethod("process", command.getClass()).invoke(this, command);
    }

    public A applyEvent(Event event) throws Exception {
        getClass().getMethod("apply", event.getClass()).invoke(this, event);
        return (A)this;
    }
    
}