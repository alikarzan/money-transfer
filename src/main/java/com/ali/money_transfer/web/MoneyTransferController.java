package com.ali.money_transfer.web;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ali.common.MoneyTransferInfo;
import com.ali.common.web.RestUtil;
import com.ali.money_transfer.MoneyTransferServie;

@Path("transfer")
public class MoneyTransferController{

    private MoneyTransferServie service;

    @Inject
    public MoneyTransferController(MoneyTransferServie service) {
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createMoneyTransfer(@Suspended final AsyncResponse asyncResponse, MoneyTransferInfo transferInfo){

        service.createTransfer(transferInfo).handle((moneyTransfer, exception)->{
                Response response;
                if(exception != null){
                    response = RestUtil.generateServerErrorResponse(exception.getMessage());
                }else{
                    response = RestUtil.generateAcceptedResponse(new MoneyTransferCreateRestResponse(RestUtil.STATE_URI + moneyTransfer.getEventId(),
                               RestUtil.RESOURCE_URI_ACCOUNT + moneyTransfer.getAggregate().getTransferInfo().getToAccountId(), RestUtil.RESOURCE_URI_ACCOUNT + moneyTransfer.getAggregate().getTransferInfo().getFromAccountId(), moneyTransfer.getAggregate()));
                }
               return response;
            }).thenAccept(resp -> asyncResponse.resume(resp));
    }
}