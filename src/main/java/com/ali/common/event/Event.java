package com.ali.common.event;

import java.util.UUID;

public class Event {
    private String id;
    private String state;
    private String description;

    public Event(){
        this.id = UUID.randomUUID().toString();
        this.state = EventState.IN_PROGRESS.name();
    }

    public String getState(){
        return this.state;
    }
    public String getDescription(){
        return this.description;
    }
    public void setSate(String state){
        this.state = state;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getId(){
        return this.id;
    }
}