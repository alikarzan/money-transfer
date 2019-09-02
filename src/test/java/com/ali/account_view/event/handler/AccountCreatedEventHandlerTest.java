package com.ali.account_view.event.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;

import com.ali.account_view.AccountViewRepository;
import com.ali.common.AccountInfo;
import com.ali.common.AccountViewInfo;
import com.ali.common.MoneyTransferInfo;
import com.ali.common.event.Event;
import com.ali.common.event.account.AccountCreatedEvent;
import com.ali.common.event.money.tarnsfer.TransferCreatedEvent;
import com.ali.infrastructure.EventStore;
import com.ali.infrastructure.http.HttpClientAdapter;

import org.apache.http.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountCreatedEventHandlerTest {

    @Mock
    private AccountViewRepository accountViewRepo;
    @Mock
    private EventStore eventStore;
    @Mock
    private HttpClientAdapter httpAdapter;
    @InjectMocks
    private AccountCreatedEventHandler accountCreatedEventHandler;

    private AccountCreatedEvent accountCreatedEvent;
    private TransferCreatedEvent transferCreatedEvent;
    private AccountViewInfo account;
    private AccountInfo accountInfo;

    @Before
    public void initialize(){
        accountInfo = new AccountInfo("1",new BigDecimal(100), "Account 1", "1");
        account = new AccountViewInfo(accountInfo);
        accountCreatedEvent = new AccountCreatedEvent(accountInfo);
        transferCreatedEvent = new TransferCreatedEvent(new MoneyTransferInfo("1","2",new BigDecimal(300)));
    }

    @Test
    public void testHandleCreateAccount() throws ParseException, IOException{

        when(accountViewRepo.getAccount("1")).thenReturn(null);
        when(httpAdapter.getResource(Matchers.anyString())).thenReturn("");

        accountCreatedEventHandler.handle(accountCreatedEvent);

        assertEquals("SUCCESS", accountCreatedEvent.getState());

        verify(accountViewRepo, times(1)).saveAccount(Matchers.any(AccountViewInfo.class));
        verify(eventStore,times(1)).update(accountCreatedEvent);
        verify(eventStore, times(1)).add(Matchers.anyListOf(Event.class));

    }

    @Test
    public void testHandleCreateAccountEventForExistingAccount(){
        
        when(accountViewRepo.getAccount("1")).thenReturn(account);

        accountCreatedEventHandler.handle(accountCreatedEvent);

        assertEquals("FAIL", accountCreatedEvent.getState());
        assertEquals("Account Already Exists", accountCreatedEvent.getDescription());

        verify(eventStore, times(1)).update(accountCreatedEvent);
        verify(accountViewRepo, never()).saveAccount(Matchers.any(AccountViewInfo.class));

    }

    @Test
    public void testHandleCreateAccountEventForNonExistingOwner() throws ParseException, IOException{
        
        when(accountViewRepo.getAccount("1")).thenReturn(null);
        when(httpAdapter.getResource(Matchers.anyString())).thenReturn(null);

        accountCreatedEventHandler.handle(accountCreatedEvent);
        
        assertEquals("FAIL", accountCreatedEvent.getState());
        assertEquals("Customer Given in Owner Does not Exists", accountCreatedEvent.getDescription());

        verify(eventStore, times(1)).update(accountCreatedEvent);
        verify(accountViewRepo, never()).saveAccount(Matchers.any(AccountViewInfo.class));

    }

    @Test
    public void testHandleEventOtherThanAccountCreated(){

        accountCreatedEventHandler.handle(transferCreatedEvent);

        verify(eventStore, never()).add(Matchers.anyListOf(Event.class));
        verify(eventStore, never()).update(transferCreatedEvent);
        verify(accountViewRepo, never()).saveAccount(Matchers.any(AccountViewInfo.class));
    }
}