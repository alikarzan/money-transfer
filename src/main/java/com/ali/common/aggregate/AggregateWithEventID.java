package com.ali.common.aggregate;

import com.ali.common.command.Command;

public class AggregateWithEventID<A extends Aggregate<A, C>, C extends Command>{

    private A aggregate;
    private String eventId;

    public AggregateWithEventID(A aggregate, String eventId){
        this.aggregate = aggregate;
        this.eventId = eventId;
    }

    public A getAggregate(){
        return this.aggregate;
    }
    public String getEventId(){
        return this.eventId;
    }

}