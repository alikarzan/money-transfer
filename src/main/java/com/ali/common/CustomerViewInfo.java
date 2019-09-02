package com.ali.common;

import java.util.List;

public class CustomerViewInfo implements ViewInfo{
    private CustomerInfo customerInfo;
    private List<AccountViewInfo> accountInfo;

    public CustomerViewInfo(){

    }

    public CustomerViewInfo(CustomerInfo customerInfo, List<AccountViewInfo> accountInfos){
        this.customerInfo = customerInfo;
        this.accountInfo = accountInfos;
    }

    public CustomerInfo getCustomerInfo(){
        return this.customerInfo;
    }
    public List<AccountViewInfo> getAccountInfo(){
        return this.accountInfo;
    }
}