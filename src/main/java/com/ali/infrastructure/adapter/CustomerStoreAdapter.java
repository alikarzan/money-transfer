package com.ali.infrastructure.adapter;

import java.util.Collection;

import javax.inject.Inject;

import com.ali.common.CustomerViewInfo;
import com.ali.infrastructure.CustomerDocumentStore;

public class CustomerStoreAdapter implements DataStoreAdapter<CustomerViewInfo> {

    private CustomerDocumentStore dataStore;

    @Inject
    public CustomerStoreAdapter(CustomerDocumentStore dataStore){
        this.dataStore = dataStore;
    }

    @Override
    public void configure() {

    }

    @Override
    public CustomerViewInfo save(CustomerViewInfo v) {
        return dataStore.save(v);
    }

    @Override
    public CustomerViewInfo get(String id) {
        return dataStore.get(id);
    }

    @Override
    public Collection<CustomerViewInfo> getAll() {
		return dataStore.getAll();
	}
    
}