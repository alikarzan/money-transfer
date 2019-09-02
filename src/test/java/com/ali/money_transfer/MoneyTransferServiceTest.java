package com.ali.money_transfer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.ali.common.MoneyTransferInfo;
import com.ali.common.aggregate.AggragateRepository;
import com.ali.common.command.Command;
import com.ali.money_transfer.command.CreateTransferCommand;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MoneyTransferServiceTest{

    @Mock
    private AggragateRepository<MoneyTransfer, Command> transferRepository;
    @InjectMocks
    private MoneyTransferServie moneyTransferService;

    @Captor
    ArgumentCaptor<CreateTransferCommand> captor;

    private BigDecimal amount = new BigDecimal(50);
    private final MoneyTransferInfo moneyTransferInfo = new MoneyTransferInfo("1", "2", amount);

    @Test
    public void testCreateMoneyTransfer(){
        moneyTransferService.createTransfer(moneyTransferInfo);

        verify(transferRepository, times(1)).saveAggregate(Matchers.eq(MoneyTransfer.class), captor.capture());
        
        assertEquals("1", captor.getValue().getTransferInfo().getToAccountId());
        assertEquals("2", captor.getValue().getTransferInfo().getFromAccountId());
        assertEquals(amount, captor.getValue().getTransferInfo().getAmount());
    
    }

}