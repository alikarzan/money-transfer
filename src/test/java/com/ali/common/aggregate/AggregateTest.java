package com.ali.common.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.ali.common.CustomerInfo;
import com.ali.common.event.Event;
import com.ali.common.event.customer.CustomerCreatedEvent;
import com.ali.customer.Customer;
import com.ali.customer.command.CreateCustomerCommand;

import org.junit.Before;
import org.junit.Test;

public class AggregateTest{

    private Customer aggregate;
    private CustomerInfo customerInfo;
    private CreateCustomerCommand command;
    private CustomerCreatedEvent event;

    @Before
    public void initialize(){
        aggregate = new Customer();
        customerInfo = new CustomerInfo("jack", "jill", "1");
        command = new CreateCustomerCommand(customerInfo);
        event = new CustomerCreatedEvent(customerInfo);
    }


    @Test
    public void testProcessCommand() throws Exception {

       List<Event> events = aggregate.processCommand(command);
       assertEquals(1, events.size());
       assertTrue(events.get(0) instanceof CustomerCreatedEvent);

    }

    @Test
    public void testApplyEvent() throws Exception {
        assertNull(aggregate.getCustomerInfo());

        aggregate = aggregate.applyEvent(event);

        assertEquals("jack", aggregate.getCustomerInfo().getFirstName());
        assertEquals("jill", aggregate.getCustomerInfo().getLastName());
        assertEquals("1", aggregate.getCustomerInfo().getSsn());

    }
}