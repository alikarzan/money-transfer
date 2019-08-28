package com.ali.account_view.web;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ali.account_view.AccountViewRepository;
import com.ali.common.AccountViewInfo;

@Path("accounts")
public class AccountViewController{

    private AccountViewRepository accountRepo;

    @Inject
    public AccountViewController(AccountViewRepository accountRepo){
        this.accountRepo = accountRepo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<AccountViewInfo> getAccounts(){
        return accountRepo.getAccounts();
    }

    @GET()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountViewInfo getAccount(@PathParam("id") String id){
        return accountRepo.getAccount(id);
    }
}