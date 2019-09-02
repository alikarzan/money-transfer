package com.ali.common.event;

public abstract class EventHandler {

    protected EventHandler nextHandler;

    public abstract void handle(Event event);
    
    public void setNextHandler(EventHandler eventHandler){
        this.nextHandler = eventHandler;
    }

}