package com.ali.account_view.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.ali.account_view.AccountViewRepository;
import com.ali.common.AccountInfo;
import com.ali.common.AccountViewInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountViewControllerTest{

    @Mock
    private AccountViewRepository accountRepo;
    @InjectMocks
    private AccountViewController accountViewController;

    private AccountViewInfo accountViewInfo;
    private String accountId;

    @Before
    public void initialize(){
      accountId = "1";
      accountViewInfo = new AccountViewInfo(new AccountInfo(accountId, new BigDecimal(100), "Account", "1"));
    }

    @Test
    public void testGettingAccounts(){
      accountViewController.getAccounts();

      verify(accountRepo, times(1)).getAccounts();
      verify(accountRepo, never()).getAccount(accountId);
    }

    @Test
    public void testGettingAnAccount(){
      when(accountRepo.getAccount(accountId)).thenReturn(accountViewInfo);

      AccountViewInfo result = accountViewController.getAccount(accountId);

      verify(accountRepo, times(1)).getAccount(accountId);

      assertEquals("1", result.getAccountInfo().getId());
      assertEquals(new BigDecimal(100), result.getAccountInfo().getBalance());

    }

}