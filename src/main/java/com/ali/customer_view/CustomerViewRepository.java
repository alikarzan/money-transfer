package com.ali.customer_view;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import com.ali.common.CustomerViewInfo;
import com.ali.infrastructure.adapter.DataStoreAdapter;

public class CustomerViewRepository{

    private DataStoreAdapter<CustomerViewInfo> customerStore;

    @Inject
    public CustomerViewRepository(@Named("customerStoreAdapter") DataStoreAdapter<CustomerViewInfo> customerStore){
        this.customerStore = customerStore;
    }

    public void saveCustomer(CustomerViewInfo customerViewInfo){
        customerStore.save(customerViewInfo);
    }

    public Collection<CustomerViewInfo> getCustomers(){
        return customerStore.getAll();
    }

    public CustomerViewInfo getCustomer(String id){
        return customerStore.get(id);
    }
    
}