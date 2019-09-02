package com.ali.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.ali.common.CustomerInfo;
import com.ali.common.event.Event;
import com.ali.common.event.customer.CustomerCreatedEvent;
import com.ali.customer.command.CreateCustomerCommand;

import org.junit.Before;
import org.junit.Test;

public class CustomerTest{

    private CreateCustomerCommand customerCreateCommand;
    private CustomerCreatedEvent customerCreatedEvent;

    private Customer customer;
    private CustomerInfo customerInfo;

    @Before
    public void initialize(){
        customerInfo = new CustomerInfo("jack", "jill", "1");
        customer = new Customer();
        customerCreateCommand = new CreateCustomerCommand(customerInfo);
        customerCreatedEvent = new CustomerCreatedEvent(customerInfo);
    }

    @Test
    public void testProcessCreateCustomerCommand(){
        List<Event> events = customer.process(customerCreateCommand);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof CustomerCreatedEvent);
    }

    @Test
    public void testApplyCustomerCreatedEvent(){
        assertNull(customer.getCustomerInfo());

        customer = customer.apply(customerCreatedEvent);

        assertEquals("jack", customer.getCustomerInfo().getFirstName());
        assertEquals("jill", customer.getCustomerInfo().getLastName());
        assertEquals("1", customer.getCustomerInfo().getSsn());
    }
}