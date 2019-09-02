package com.ali.infrastructure.adapter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.ali.common.AccountInfo;
import com.ali.common.AccountViewInfo;
import com.ali.infrastructure.AccountDocumentStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountStoreAdapterTest{

    @Mock
    private AccountDocumentStore dataStore;
    @InjectMocks
    private AccountStoreAdapter accountStoreAdapter;

    private AccountViewInfo accountViewInfo;
    private AccountInfo accountInfo;

    @Before
    public void initialize(){
        accountInfo = new AccountInfo("1", new BigDecimal(100), "Account", "1");
        accountViewInfo = new AccountViewInfo(accountInfo);
    }

    @Test
    public void testSave(){
        accountStoreAdapter.save(accountViewInfo);

        verify(dataStore, times(1)).save(accountViewInfo);
    }
    
    @Test
    public void testGetAccount(){
        accountStoreAdapter.get(accountInfo.getId());

        verify(dataStore, times(1)).get(accountInfo.getId());
    }

    @Test
    public void testGetAllAccounts(){
        accountStoreAdapter.getAll();

        verify(dataStore, times(1)).getAll();
    }

}