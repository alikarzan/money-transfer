package com.ali.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.ali.account.command.CreateAccountCommand;
import com.ali.common.AccountInfo;
import com.ali.common.aggregate.AggragateRepository;
import com.ali.common.command.Command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest{
   
    @Mock
    private AggragateRepository<Account, Command> accountRepository;
    @InjectMocks
    private AccountService accountService;

    @Captor
    ArgumentCaptor<CreateAccountCommand> captor;

    private BigDecimal balance = new BigDecimal(100);
    private final AccountInfo accountInfo = new AccountInfo("1", balance, "Account","1");

    @Test
    public void testCreateAccount(){
        accountService.createAccount(accountInfo);

        verify(accountRepository, times(1)).saveAggregate(Matchers.eq(Account.class), captor.capture());
        
        assertEquals("1", captor.getValue().getAccountInfo().getId());
        assertEquals(balance, captor.getValue().getAccountInfo().getBalance());
        assertEquals("Account", captor.getValue().getAccountInfo().getTitle());
        assertEquals("1", captor.getValue().getAccountInfo().getOwner());

    }

}