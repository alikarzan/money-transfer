package com.ali.money_transfer;

import java.util.Arrays;
import java.util.List;

import com.ali.common.aggregate.Aggregate;
import com.ali.common.command.Command;
import com.ali.common.event.Event;
import com.ali.common.MoneyTransferInfo;
import com.ali.common.event.money.tarnsfer.TransferCreatedEvent;
import com.ali.money_transfer.command.CreateTransferCommand;

public class MoneyTransfer extends Aggregate<MoneyTransfer, Command>{

    private MoneyTransferInfo transferInfo;

    public List<Event> process(CreateTransferCommand command){
        return Arrays.asList(new TransferCreatedEvent(command.getTransferInfo()));
    }

    public MoneyTransfer apply(TransferCreatedEvent event){
        this.transferInfo = event.getTransferInfo();
        return this;
    }

    public MoneyTransferInfo getTransferInfo(){
        return this.transferInfo;
    }
}