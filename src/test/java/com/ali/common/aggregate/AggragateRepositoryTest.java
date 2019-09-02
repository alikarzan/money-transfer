package com.ali.common.aggregate;

import com.ali.customer.command.CreateCustomerCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.ali.common.CustomerInfo;
import com.ali.common.command.Command;
import com.ali.common.event.Event;
import com.ali.common.event.customer.CustomerCreatedEvent;
import com.ali.customer.Customer;
import com.ali.infrastructure.EventStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AggragateRepositoryTest{

    @Mock
    private EventStore eventStore;
    @InjectMocks
    private AggragateRepository<Customer, Command> aggregateRepository;
    @Captor
    private ArgumentCaptor<List<Event>> captor;

    private CreateCustomerCommand customerCreateCommand;

    @Before
    public void initialize(){
        customerCreateCommand = new CreateCustomerCommand(new CustomerInfo("jack", "jill", "1"));
    }

    @Test
    public void testSaveAggregate(){
        CompletableFuture<AggregateWithEventID<Customer, Command>> result = aggregateRepository.saveAggregate(Customer.class, customerCreateCommand);
        result.thenAccept(aggregateWithId->{
            verify(eventStore, times(1)).add(Matchers.anyListOf(Event.class));
            verify(eventStore, times(1)).add(captor.capture());

            List<Event> events = captor.getValue();

            assertTrue(events.get(0) instanceof CustomerCreatedEvent);
            assertEquals(events.get(0).getId(), aggregateWithId.getEventId());

            assertEquals("jack", aggregateWithId.getAggregate().getCustomerInfo().getFirstName());

            assertEquals("jack", ((CustomerCreatedEvent)events.get(0)).getCustomerInfo().getFirstName());
            assertEquals("jill", ((CustomerCreatedEvent)events.get(0)).getCustomerInfo().getLastName());
            assertEquals("1", ((CustomerCreatedEvent)events.get(0)).getCustomerInfo().getSsn());
        });
    }

}