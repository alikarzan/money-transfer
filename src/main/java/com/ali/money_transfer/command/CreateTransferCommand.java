package com.ali.money_transfer.command;

import com.ali.common.command.Command;
import com.ali.common.MoneyTransferInfo;

public class CreateTransferCommand extends Command{
    private MoneyTransferInfo transferInfo;

    public CreateTransferCommand(MoneyTransferInfo transferInfo){
        this.transferInfo = transferInfo;
    }

    public MoneyTransferInfo getTransferInfo(){
        return this.transferInfo;
    }
}