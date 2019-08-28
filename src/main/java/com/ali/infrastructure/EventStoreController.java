package com.ali.infrastructure;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ali.common.event.Event;

@Path("state")
public class EventStoreController{

    private EventStore eventStore;

    @Inject
    public EventStoreController(EventStore eventStore){
        this.eventStore = eventStore;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Event getEventState(@PathParam("id") String id){
        return eventStore.getEvent(id);
    }

}