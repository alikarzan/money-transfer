package com.ali.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import com.ali.account.command.CreateAccountCommand;
import com.ali.common.AccountInfo;
import com.ali.common.event.Event;
import com.ali.common.event.account.AccountCreatedEvent;

import org.junit.Before;
import org.junit.Test;

public class AccountTest {

    private CreateAccountCommand accountCreateCommand;
    private AccountCreatedEvent accountCreatedEvent;

    private Account account;
    private AccountInfo accountInfo;
    private BigDecimal balance;

    @Before
    public void initialize(){
        balance = new BigDecimal(100);
        accountInfo = new AccountInfo("1", balance, "Account", "1");
        account = new Account();
        accountCreateCommand = new CreateAccountCommand(accountInfo);
        accountCreatedEvent = new AccountCreatedEvent(accountInfo);
    }

    @Test
    public void testProcessCreateAccountCommand(){
        List<Event> events = account.process(accountCreateCommand);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof AccountCreatedEvent);
    }

    @Test
    public void testApplyAccountCreatedEvent(){
        assertNull(account.getAccountInfo());

        account = account.apply(accountCreatedEvent);

        assertEquals("1", accountCreatedEvent.getAccountInfo().getId());
        assertEquals(balance, accountCreatedEvent.getAccountInfo().getBalance());
        assertEquals("Account", accountCreatedEvent.getAccountInfo().getTitle());
        assertEquals("1", accountCreatedEvent.getAccountInfo().getOwner());
    }

}