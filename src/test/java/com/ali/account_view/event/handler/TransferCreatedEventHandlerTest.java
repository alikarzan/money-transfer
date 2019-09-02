package com.ali.account_view.event.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.ali.account_view.AccountViewRepository;
import com.ali.account_view.event.handler.TransferCreatedEventHandler;
import com.ali.common.AccountInfo;
import com.ali.common.AccountViewInfo;
import com.ali.common.MoneyTransferInfo;
import com.ali.common.event.Event;
import com.ali.common.event.account.AccountCreatedEvent;
import com.ali.common.event.money.tarnsfer.TransferCreatedEvent;
import com.ali.infrastructure.EventStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransferCreatedEventHandlerTest {

    @Mock
    private AccountViewRepository accountViewRepo;
    @Mock
    private EventStore eventStore;

    @InjectMocks
    private TransferCreatedEventHandler transferEventHandler;// = new TransferCreatedEventHandler(accountViewRepo,
            //eventStore);

    private TransferCreatedEvent transferEvent;
    private AccountCreatedEvent accountCreatedEvent;
    private AccountViewInfo toAccount;
    private AccountViewInfo fromAccount;
    private AccountViewInfo insufficientBalanceFromAccount;
    private BigDecimal transferAmount;

    @Before
    public void initialize(){
        
        toAccount = new AccountViewInfo(new AccountInfo("1",new BigDecimal(100), "Account 1", "1"));
        fromAccount = new AccountViewInfo(new AccountInfo("2",new BigDecimal(200), "Account 2", "2"));
        transferAmount = new BigDecimal(50);

        insufficientBalanceFromAccount = new AccountViewInfo(new AccountInfo("2",new BigDecimal(20), "Account 2", "2"));

        transferEvent = new TransferCreatedEvent(new MoneyTransferInfo("1", "2",transferAmount));
        accountCreatedEvent = new AccountCreatedEvent(new AccountInfo("1", transferAmount, "New Account", "1"));
    }

    @Test
    public void testHandle(){

        when(accountViewRepo.getAccount("1")).thenReturn(toAccount);
        when(accountViewRepo.getAccount("2")).thenReturn(fromAccount);

        transferEventHandler.handle(transferEvent);

        assertEquals("SUCCESS", transferEvent.getState());

        verify(eventStore, times(1)).update(transferEvent);
        verify(eventStore, times(2)).add(Matchers.anyListOf(Event.class));
    
        verify(accountViewRepo, times(2)).saveAccount(Matchers.any(AccountViewInfo.class));

    }

    @Test
    public void testHandleWithInsufficientFromBalance(){

        when(accountViewRepo.getAccount("1")).thenReturn(toAccount);
        when(accountViewRepo.getAccount("2")).thenReturn(insufficientBalanceFromAccount);

        transferEventHandler.handle(transferEvent);

        assertEquals("FAIL", transferEvent.getState());
        assertEquals("Insufficient balance in From Account for transferring money", transferEvent.getDescription());

        verify(eventStore, times(1)).update(transferEvent);

    }

    @Test
    public void testHandleWithInvalidFromAcount(){

        when(accountViewRepo.getAccount("1")).thenReturn(toAccount);
        when(accountViewRepo.getAccount("2")).thenReturn(null);

        transferEventHandler.handle(transferEvent);

        assertEquals("FAIL", transferEvent.getState());
        assertEquals("Invalid account, no such account as : fromAccount: 2", transferEvent.getDescription());


    }

    @Test
    public void testHandleWithInvalidToAcount(){
        when(accountViewRepo.getAccount("1")).thenReturn(null);
        when(accountViewRepo.getAccount("2")).thenReturn(fromAccount);

        transferEventHandler.handle(transferEvent);

        assertEquals("FAIL", transferEvent.getState());
        assertEquals("Invalid account, no such account as : toAccount: 1", transferEvent.getDescription());
    }

    @Test
    public void testHandleWithInvalidAcounts(){

        when(accountViewRepo.getAccount("1")).thenReturn(null);
        when(accountViewRepo.getAccount("2")).thenReturn(null);

        transferEventHandler.handle(transferEvent);

        assertEquals("FAIL", transferEvent.getState());
        assertEquals("Invalid account, no such account as : toAccount: 1 and fromAccount: 2", transferEvent.getDescription());

    }

    @Test
    public void testHandleEventOtherThanTransferCreated(){
        transferEventHandler.handle(accountCreatedEvent);

        verify(accountViewRepo, never()).getAccount(Matchers.anyString());
        verify(eventStore, never()).update(Matchers.any(Event.class));
        verify(eventStore, never()).add(Matchers.anyListOf(Event.class));
    }




}