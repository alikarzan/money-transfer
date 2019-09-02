package com.ali.customer_view.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ali.common.CustomerInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.customer_view.CustomerViewRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerViewControllerTest{

    @Mock
    private CustomerViewRepository customerRepo;
    @InjectMocks
    private CustomerViewController customerViewController;

    private CustomerViewInfo customerViewInfo;
    private String customerId;

    @Before
    public void initialize(){
        customerId = "1";
        customerViewInfo = new CustomerViewInfo(new CustomerInfo("jack", "Jill", customerId), null);
    }

    @Test
    public void testGettingCustomers(){
        customerViewController.getCustomers();

        verify(customerRepo, times(1)).getCustomers();
    }

    @Test
    public void testGettingACustomer(){
        when(customerRepo.getCustomer(customerId)).thenReturn(customerViewInfo);

        CustomerViewInfo result = customerViewController.getCustomer(customerId);

        verify(customerRepo, times(1)).getCustomer(customerId);

        assertEquals("jack", result.getCustomerInfo().getFirstName());
        assertNull(result.getAccountInfo());
    }

}