package com.ali.money_transfer;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import com.ali.common.aggregate.AggragateRepository;
import com.ali.common.aggregate.AggregateWithEventID;
import com.ali.common.command.Command;
import com.ali.common.MoneyTransferInfo;
import com.ali.money_transfer.command.CreateTransferCommand;

public class MoneyTransferServie {

    private AggragateRepository<MoneyTransfer, Command> transferRepository;

    @Inject
    public MoneyTransferServie(AggragateRepository<MoneyTransfer, Command> transferRepository){
        this.transferRepository = transferRepository;
    }

    public CompletableFuture<AggregateWithEventID<MoneyTransfer, Command>> createTransfer(MoneyTransferInfo transferInfo) {
        return transferRepository.saveAggregate(MoneyTransfer.class, new CreateTransferCommand(transferInfo));
                                  //.thenApply(aggregate->(MoneyTransfer)aggregate);
    }
}