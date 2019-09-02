package com.ali.customer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ali.common.CustomerInfo;
import com.ali.common.aggregate.AggragateRepository;
import com.ali.common.command.Command;
import com.ali.customer.command.CreateCustomerCommand;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest{

    @Mock
    private AggragateRepository<Customer, Command> customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @Captor
    ArgumentCaptor<CreateCustomerCommand> captor;

    private final CustomerInfo customerInfo = new CustomerInfo("jack", "jill","1");

    @Test
    public void testCreateCustomer(){
        customerService.createCustomer(customerInfo);

        verify(customerRepository, times(1)).saveAggregate(Matchers.eq(Customer.class), captor.capture());
        
        assertEquals("jack", captor.getValue().getCustomerInfo().getFirstName());
        assertEquals("jill", captor.getValue().getCustomerInfo().getLastName());
        assertEquals("1", captor.getValue().getCustomerInfo().getSsn());
    
    }


}