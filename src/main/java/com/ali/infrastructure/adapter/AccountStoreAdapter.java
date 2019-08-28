package com.ali.infrastructure.adapter;

import java.util.Collection;

import javax.inject.Inject;

import com.ali.common.AccountViewInfo;
import com.ali.infrastructure.AccountDocumentStore;

public class AccountStoreAdapter implements DataStoreAdapter<AccountViewInfo> {

    private AccountDocumentStore dataStore;

    @Inject
    public AccountStoreAdapter(AccountDocumentStore dataStore){
        this.dataStore = dataStore;
    }
    
    
    @Override
    public void configure() {

    }

    @Override
    public AccountViewInfo save(AccountViewInfo v) {
        return dataStore.save(v);
    }

    @Override
    public AccountViewInfo get(String id) {
        return dataStore.get(id);
    }

    @Override
    public Collection<AccountViewInfo> getAll() {
		return dataStore.getAll();
	}
    
}