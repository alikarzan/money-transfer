package com.ali.common.event.money.tarnsfer;

import com.ali.common.event.Event;
import com.ali.common.MoneyTransferInfo;

public class TransferCreatedEvent extends Event{
    private MoneyTransferInfo transferInfo;

    public TransferCreatedEvent(MoneyTransferInfo transferInfo){
        this.transferInfo = transferInfo;
    }

    public MoneyTransferInfo getTransferInfo(){
        return this.transferInfo;
    }
}