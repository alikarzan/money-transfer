package com.ali.common.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EventTest{

    @Test
    public void testCreateEventWithIdAndInProgressState(){
        Event event = new Event();

        assertEquals("IN_PROGRESS", event.getState());
        assertNotNull(event.getId());
        assertFalse(event.getId().isEmpty());
    }
}