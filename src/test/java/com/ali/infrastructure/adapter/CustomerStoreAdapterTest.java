package com.ali.infrastructure.adapter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ali.common.CustomerInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.infrastructure.CustomerDocumentStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerStoreAdapterTest{

    @Mock
    private CustomerDocumentStore customerDocumentStore;
    @InjectMocks
    private CustomerStoreAdapter customerStoreAdapter;

    private CustomerViewInfo customerViewInfo;
    private CustomerInfo customerInfo;

    @Before
    public void initialize(){
        customerInfo = new CustomerInfo("jack", "jill", "1");
        customerViewInfo = new CustomerViewInfo(customerInfo, null);
    }

    @Test
    public void testSaveCustomer(){
        customerStoreAdapter.save(customerViewInfo);

        verify(customerDocumentStore, times(1)).save(customerViewInfo);
    }

    @Test
    public void testGetCustomer(){
        customerStoreAdapter.get(customerInfo.getSsn());

        verify(customerDocumentStore, times(1)).get(customerInfo.getSsn());
    }

    @Test
    public void testGetAllCustomers(){
        customerStoreAdapter.getAll();

        verify(customerDocumentStore, times(1)).getAll();
    }
    
}