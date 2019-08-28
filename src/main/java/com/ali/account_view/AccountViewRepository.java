package com.ali.account_view;

import java.util.Collection;

import javax.inject.Inject;

import com.ali.common.AccountViewInfo;
import com.ali.infrastructure.adapter.DataStoreAdapter;
import com.google.inject.name.Named;

public class AccountViewRepository{

    private DataStoreAdapter<AccountViewInfo> accountStore;

    @Inject
    public AccountViewRepository(@Named("accountStoreAdapter") DataStoreAdapter<AccountViewInfo> accountStore){
        this.accountStore = accountStore;
    }

    public void saveAccount(AccountViewInfo accountViewInfo){
        accountStore.save(accountViewInfo);

    }
    public Collection<AccountViewInfo> getAccounts(){
        return accountStore.getAll();
    }

    public AccountViewInfo getAccount(String id){
        return accountStore.get(id);
    }
}