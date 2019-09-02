package com.ali.account.web;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ali.account.AccountService;
import com.ali.common.AccountInfo;
import com.ali.common.web.RestUtil;

@Path("account")
public class AccountController{
   
    private AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createAccount(@Suspended final AsyncResponse asyncResponse, @NotNull @Valid AccountInfo accountInfo){

          accountService.createAccount(accountInfo).handle((account, exception)->{
                Response response;
                if(exception != null){
                    response = RestUtil.generateServerErrorResponse(exception.getMessage());
                }else{
                    response = RestUtil.generateAcceptedResponse(new AccountCreateRestResponse(RestUtil.STATE_URI+account.getEventId(), RestUtil.RESOURCE_URI_ACCOUNT+account.getAggregate().getAccountInfo().getId(), account.getAggregate()));
                }
                return response;
            }).thenAccept(resp-> asyncResponse.resume(resp));
    }


}