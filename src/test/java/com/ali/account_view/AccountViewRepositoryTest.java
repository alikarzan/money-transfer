package com.ali.account_view;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.ali.common.AccountInfo;
import com.ali.common.AccountViewInfo;
import com.ali.infrastructure.adapter.AccountStoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountViewRepositoryTest{

    @Mock
    private AccountStoreAdapter accountStoreAdapter;
    @InjectMocks
    private AccountViewRepository accountViewRepository;

    private AccountViewInfo accuntViewInfo;

    @Before
    public void initialize(){
        accuntViewInfo = new AccountViewInfo(new AccountInfo("1", new BigDecimal(100), "Account", "1"));
    }


    @Test
    public void testSaveAccount(){
        accountViewRepository.saveAccount(accuntViewInfo);

        verify(accountStoreAdapter, times(1)).save(accuntViewInfo);
        verify(accountStoreAdapter, never()).getAll();
        verify(accountStoreAdapter, never()).get("1");
    }

    @Test
    public void testGettingAllAccounts(){
        accountViewRepository.getAccounts();

        verify(accountStoreAdapter, times(1)).getAll();
        verify(accountStoreAdapter, never()).save(accuntViewInfo);
        verify(accountStoreAdapter, never()).get("1");
    }

    @Test
    public void testGettingAnAccount(){
        accountViewRepository.getAccount("1");

        verify(accountStoreAdapter, times(1)).get("1");
        verify(accountStoreAdapter, never()).getAll();
        verify(accountStoreAdapter, never()).save(accuntViewInfo);
    }

}