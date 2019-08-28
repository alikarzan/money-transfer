package com.ali.account_view.event.handler;

import java.util.Arrays;

import javax.inject.Inject;

import com.ali.account_view.AccountViewRepository;
import com.ali.common.AccountInfo;
import com.ali.common.AccountViewInfo;
import com.ali.common.event.Event;
import com.ali.common.event.EventHandler;
import com.ali.common.event.EventState;
import com.ali.common.MoneyTransferInfo;
import com.ali.common.event.account.AccountUpdatedEvent;
import com.ali.infrastructure.EventStore;
import com.ali.common.event.money.tarnsfer.TransferCreatedEvent;

public class TransferCreatedEventHandler extends EventHandler{

    private AccountViewRepository accountViewRepo;
    private EventStore eventStore;

    @Inject
    public TransferCreatedEventHandler(AccountViewRepository accountViewRepo, EventStore eventStore){
        this.accountViewRepo = accountViewRepo;
        this.eventStore = eventStore;
    }

    @Override
    public void handle(Event event) {
        if(event instanceof TransferCreatedEvent){
            System.out.println("%%% inside Account Transfer notif %%%");
            TransferCreatedEvent event2 = (TransferCreatedEvent)event;
            MoneyTransferInfo transferInfo = event2.getTransferInfo();

            AccountViewInfo toAccount = accountViewRepo.getAccount(transferInfo.getToAccountId());
            AccountViewInfo fromAccount = accountViewRepo.getAccount(transferInfo.getFromAccountId());

            if(toAccount != null && fromAccount != null){
                if(fromAccount.getAccountInfo().getBalance().compareTo(event2.getTransferInfo().getAmount()) != -1){
 
                    AccountInfo updatedToAccount = new AccountInfo(toAccount.getAccountInfo().getId(), 
                                                                toAccount.getAccountInfo().getBalance().add(transferInfo.getAmount()),
                                                                toAccount.getAccountInfo().getTitle(),
                                                                toAccount.getAccountInfo().getOwner());
                    AccountViewInfo updatedToAccountView = new AccountViewInfo(updatedToAccount);

                    AccountInfo updatedFromAccount = new AccountInfo(fromAccount.getAccountInfo().getId(), 
                    fromAccount.getAccountInfo().getBalance().subtract(transferInfo.getAmount()),
                    fromAccount.getAccountInfo().getTitle(),
                    fromAccount.getAccountInfo().getOwner());
                    AccountViewInfo updatedFromAccountView = new AccountViewInfo(updatedFromAccount);

                    accountViewRepo.saveAccount(updatedToAccountView);
                    accountViewRepo.saveAccount(updatedFromAccountView);

                    eventStore.add(Arrays.asList(new AccountUpdatedEvent(updatedToAccount)));
                    eventStore.add(Arrays.asList(new AccountUpdatedEvent(updatedFromAccount)));

                    event.setSate(EventState.SUCCESS.name());
                    eventStore.update(event);

                }else{
                    updateFailedEvents("Insufficient balance for transferring account", event);
                }
            }else{
                String description = "Invalid account there is no such account as : "+toAccount == null?event2.getTransferInfo().getToAccountId():""+fromAccount == null?event2.getTransferInfo().getFromAccountId():"";
                updateFailedEvents(description, event);
            }
        }else if(nextHandler != null){
            nextHandler.handle(event);
        }
    }
    
    private void updateFailedEvents(String description, Event event){
        event.setSate(EventState.FAIL.name());
        event.setDescription(description);
        eventStore.update(event);
    }
}