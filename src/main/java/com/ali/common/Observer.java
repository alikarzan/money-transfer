package com.ali.common;

import java.util.List;

import com.ali.common.event.Event;

public interface Observer{
    void notify(List<Event> events);
}