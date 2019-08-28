package com.ali.common;

import java.util.List;

public class CustomerViewInfo implements ViewInfo{
    private CustomerInfo customerInfo;
    private List<AccountViewInfo> accountInfos;

    public CustomerViewInfo(){

    }

    public CustomerViewInfo(CustomerInfo customerInfo, List<AccountViewInfo> accountInfos){
        this.customerInfo = customerInfo;
        this.accountInfos = accountInfos;
      /*  this.accountInfos = new HashMap<>();
        if(accountInfo != null){
            this.accountInfo.put(accountInfo.getAccountInfo().getId(), accountInfo);
        }*/
    }

    public CustomerInfo getCustomerInfo(){
        return this.customerInfo;
    }
    public List<AccountViewInfo> getAccountInfo(){
        return this.accountInfos;
    }
}