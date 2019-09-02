package com.ali.money_transfer.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import com.ali.common.MoneyTransferInfo;
import com.ali.common.aggregate.AggregateWithEventID;
import com.ali.common.command.Command;
import com.ali.common.event.money.tarnsfer.TransferCreatedEvent;
import com.ali.money_transfer.MoneyTransfer;
import com.ali.money_transfer.MoneyTransferServie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransferControllerTest{

    @Mock
    private MoneyTransferServie moneyTransferService;
    @Mock
    private AsyncResponse asyncResponse;
    @InjectMocks
    private MoneyTransferController moneyTransferController;

    @Captor
    ArgumentCaptor<Response> captor;

    private MoneyTransferInfo moneyTransferInfo;
    private TransferCreatedEvent event;
    private AggregateWithEventID<MoneyTransfer, Command> aggregate;
    private BigDecimal amount;

    @Before
    public void initialize(){
        amount = new BigDecimal(50);
        moneyTransferInfo = new MoneyTransferInfo("1", "2", amount);
        event = new TransferCreatedEvent(moneyTransferInfo);
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer = moneyTransfer.apply(event);
        aggregate = new AggregateWithEventID<MoneyTransfer, Command>(moneyTransfer, "123");
    }

    @Test
    public void testCreateMoneyTransfer(){
        when(moneyTransferService.createTransfer(moneyTransferInfo)).thenReturn(CompletableFuture.supplyAsync(()->aggregate));

        moneyTransferController.createMoneyTransfer(asyncResponse, moneyTransferInfo);

        verify(moneyTransferService, times(1)).createTransfer(moneyTransferInfo);

        verify(asyncResponse, times(1)).resume(captor.capture());
        Response response = captor.getValue();
        MoneyTransferCreateRestResponse resp = (MoneyTransferCreateRestResponse)response.getEntity();

        assertEquals(202, response.getStatus());
        assertEquals("/state/123", resp.getStateUri());
        assertEquals("/accounts/1", resp.getResourceUri());
        assertEquals("/accounts/2", resp.getResourceUri_2());
        assertEquals("1", resp.getMoneyTransfer().getTransferInfo().getToAccountId());
        assertEquals("2", resp.getMoneyTransfer().getTransferInfo().getFromAccountId());
        assertEquals(amount, resp.getMoneyTransfer().getTransferInfo().getAmount());
    }

    @Test
    public void testCreateMoneyTransferExceptionally() throws InterruptedException {
        when(moneyTransferService.createTransfer(moneyTransferInfo)).thenReturn(CompletableFuture.supplyAsync(()-> {
            throw new CompletionException(new Exception("Money Transfer create Exception"));
        }));

        moneyTransferController.createMoneyTransfer(asyncResponse, moneyTransferInfo);

        verify(moneyTransferService, times(1)).createTransfer(moneyTransferInfo);

        //to compansate asynchronacy
        Thread.sleep(1000);

        verify(asyncResponse, times(1)).resume(captor.capture());
        Response response = captor.getValue();
        String resp = (String)response.getEntity();

        assertEquals(500, response.getStatus());
        assertEquals("java.lang.Exception: Money Transfer create Exception", resp);

    }

}