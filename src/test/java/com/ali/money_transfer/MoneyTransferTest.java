package com.ali.money_transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import com.ali.common.MoneyTransferInfo;
import com.ali.common.event.Event;
import com.ali.common.event.money.tarnsfer.TransferCreatedEvent;
import com.ali.money_transfer.command.CreateTransferCommand;

import org.junit.Before;
import org.junit.Test;

public class MoneyTransferTest{

    private CreateTransferCommand createTransferCommand;
    private TransferCreatedEvent transferCreatedEvent;

    private MoneyTransfer moneyTransfer;
    private MoneyTransferInfo moneyTransferInfo;
    private BigDecimal amount;

    @Before
    public void initialize(){
        amount = new BigDecimal(50);
        moneyTransferInfo = new MoneyTransferInfo("1", "2", amount);
        moneyTransfer = new MoneyTransfer();
        createTransferCommand = new CreateTransferCommand(moneyTransferInfo);
        transferCreatedEvent = new TransferCreatedEvent(moneyTransferInfo);
    }

    @Test
    public void testProcessCreateTransferCommand(){
        List<Event> events = moneyTransfer.process(createTransferCommand);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof TransferCreatedEvent);
    }

    @Test
    public void testApplyCustomerCreatedEvent(){
        assertNull(moneyTransfer.getTransferInfo());

        moneyTransfer = moneyTransfer.apply(transferCreatedEvent);

        assertEquals("1", moneyTransfer.getTransferInfo().getToAccountId());
        assertEquals("2", moneyTransfer.getTransferInfo().getFromAccountId());
        assertEquals(amount, moneyTransfer.getTransferInfo().getAmount());
    }
}