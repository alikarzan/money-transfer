package com.ali.account.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.ali.account.Account;
import com.ali.account.AccountService;
import com.ali.common.AccountInfo;
import com.ali.common.aggregate.AggregateWithEventID;
import com.ali.common.command.Command;
import com.ali.common.event.account.AccountCreatedEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest{

    @Mock
    private AccountService accountService;
    @Mock
    private AsyncResponse asyncResponse;
    @InjectMocks
    private AccountController accountController;

    @Captor
    ArgumentCaptor<Response> captor;

    private AccountInfo accountInfo;
    private AggregateWithEventID<Account, Command> aggregate;
    private AccountCreatedEvent event;
    private BigDecimal balance;

    @Before
    public void initialize(){
        balance = new BigDecimal(100);
        accountInfo = new AccountInfo("1", balance, "Account", "1");
        event = new AccountCreatedEvent(accountInfo);
        Account account = new Account();
        account = account.apply(event);
        aggregate = new AggregateWithEventID<Account, Command>(account, "123");
    }

    @Test
    public void testCreateAccount() throws InterruptedException {
        when(accountService.createAccount(accountInfo)).thenReturn(CompletableFuture.supplyAsync(()->aggregate));

        accountController.createAccount(asyncResponse, accountInfo);

        verify(accountService, times(1)).createAccount(accountInfo);

        //to compansate asynchronacy
        Thread.sleep(1000);
        
        verify(asyncResponse, times(1)).resume(captor.capture());
        Response response = captor.getValue();
        AccountCreateRestResponse resp = (AccountCreateRestResponse)response.getEntity();

        assertEquals(202, response.getStatus());
        assertEquals("/state/123", resp.getStateUri());
        assertEquals("/accounts/1", resp.getResourceUri());
        assertEquals("1", resp.getAccount().getAccountInfo().getId());
        assertEquals(balance, resp.getAccount().getAccountInfo().getBalance());
        assertEquals("Account", resp.getAccount().getAccountInfo().getTitle());
        assertEquals("1", resp.getAccount().getAccountInfo().getOwner());
    }

    @Test
    public void testCreateCustomerExceptionally() throws InterruptedException{
        when(accountService.createAccount(accountInfo)).thenReturn(CompletableFuture.supplyAsync(()-> {
            throw new CompletionException(new Exception("Account create Exception"));
        }));

        accountController.createAccount(asyncResponse, accountInfo);

        verify(accountService, times(1)).createAccount(accountInfo);

        //to compansate asynchronacy
        Thread.sleep(1000);
        verify(asyncResponse, times(1)).resume(captor.capture());
        Response response = captor.getValue();
        String resp = (String)response.getEntity();

        assertEquals(500, response.getStatus());
        assertEquals("java.lang.Exception: Account create Exception", resp);

    }

}