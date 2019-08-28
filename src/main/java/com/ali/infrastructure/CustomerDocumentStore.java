package com.ali.infrastructure;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.ali.common.CustomerViewInfo;

@Singleton
public class CustomerDocumentStore {
    private Map<String, CustomerViewInfo> customerViewInfo;

    @PostConstruct
    private void init(){
        this.customerViewInfo = new ConcurrentHashMap<>();
    }

    public CustomerViewInfo get(String id){
        return customerViewInfo.get(id);
    }

    public Collection<CustomerViewInfo> getAll(){
        return customerViewInfo.values();
    }

    public CustomerViewInfo save(CustomerViewInfo customerViewInfo){
        this.customerViewInfo.put (customerViewInfo.getCustomerInfo().getSsn(), customerViewInfo);
        return customerViewInfo;
    }

    public void printDocumentStore(){
        for(Entry<String, CustomerViewInfo> entry : customerViewInfo.entrySet()){
            System.out.println(entry.getKey() +" - "+entry.getValue());
        }
    }

}