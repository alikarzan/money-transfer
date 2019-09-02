package com.ali.customer_view;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ali.common.CustomerInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.infrastructure.adapter.CustomerStoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerViewRepositoryTest{

    @Mock
    private CustomerStoreAdapter customerStoreAdapter;
    @InjectMocks
    private CustomerViewRepository customerViewRepo;

    private CustomerViewInfo customerViewInfo;
    private String customerId;

    @Before
    public void initialize(){
        customerId = "1";
        customerViewInfo = new CustomerViewInfo(new CustomerInfo("jack", "Jill", customerId), null);
    }

    @Test
    public void testSaveAccount(){
        customerViewRepo.saveCustomer(customerViewInfo);

        verify(customerStoreAdapter, times(1)).save(customerViewInfo);
        verify(customerStoreAdapter, never()).getAll();
        verify(customerStoreAdapter, never()).get("1");
    }

    @Test
    public void testGettingAllAccounts(){
        customerViewRepo.getCustomers();

        verify(customerStoreAdapter, times(1)).getAll();
        verify(customerStoreAdapter, never()).save(customerViewInfo);
        verify(customerStoreAdapter, never()).get(customerId);
    }

    @Test
    public void testGettingAnAccount(){
        customerViewRepo.getCustomer(customerId);

        verify(customerStoreAdapter, times(1)).get("1");
        verify(customerStoreAdapter, never()).getAll();
        verify(customerStoreAdapter, never()).save(customerViewInfo);
    }
}