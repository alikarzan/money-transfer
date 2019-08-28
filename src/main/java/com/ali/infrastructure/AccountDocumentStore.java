package com.ali.infrastructure;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.ali.common.AccountViewInfo;


@Singleton
public class AccountDocumentStore {
    private Map<String, AccountViewInfo> accountViewInfo;

    @PostConstruct
    private void init(){
        this.accountViewInfo = new ConcurrentHashMap<>();
    }

    public AccountViewInfo get(String id){
        return accountViewInfo.get(id);
    }

    public Collection<AccountViewInfo> getAll(){
        return accountViewInfo.values();
    }

    public AccountViewInfo save(AccountViewInfo accountViewInfo){
        this.accountViewInfo.put (accountViewInfo.getAccountInfo().getId(), accountViewInfo);
        return accountViewInfo;
    }

    public void printDocumentStore(){
        for(Entry<String, AccountViewInfo> entry : accountViewInfo.entrySet()){
            System.out.println(entry.getKey() +" - "+entry.getValue());
        }
    }

}