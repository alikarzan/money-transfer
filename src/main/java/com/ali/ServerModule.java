package com.ali;

import com.ali.common.AccountViewInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.infrastructure.adapter.AccountStoreAdapter;
import com.ali.infrastructure.adapter.CustomerStoreAdapter;
import com.ali.infrastructure.adapter.DataStoreAdapter;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;


public class ServerModule implements Module {

    @Override
    public void configure(Binder binder) {
        
        binder.bind(new TypeLiteral<DataStoreAdapter<CustomerViewInfo>>(){}).annotatedWith(Names.named("customerStoreAdapter")).to(CustomerStoreAdapter.class);
        binder.bind(new TypeLiteral<DataStoreAdapter<AccountViewInfo>>(){}).annotatedWith(Names.named("accountStoreAdapter")).to(AccountStoreAdapter.class);

    }
    

}