package com.ali.concurrency;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import com.ali.MoneytransferApplication;
import com.ali.MoneytransferConfiguration;
import com.ali.common.AccountViewInfo;
import com.ali.integration.AccountServiceIT;
import com.ali.integration.CustomerServiceIT;
import com.ali.integration.MoneyTransferServiceIT;

import org.cactoos.matchers.RunsInThreads;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import io.dropwizard.testing.junit.DropwizardAppRule;

public class MoneyTransferServiceConcurrencyIT{

    @ClassRule
    public static final DropwizardAppRule<MoneytransferConfiguration> RULE =
            new DropwizardAppRule<>(MoneytransferApplication.class, "config.yml");

    private CustomerServiceIT customerServiceIT;
    private AccountServiceIT accountServiceIT;
    private MoneyTransferServiceIT moneyTransferServiceIT;

    @Before
    public void initialize(){
        customerServiceIT = new CustomerServiceIT();
        accountServiceIT = new AccountServiceIT();
        moneyTransferServiceIT = new MoneyTransferServiceIT();
    }

    @Test
    public void testCreateMoneyTransfer(){
        MatcherAssert.assertThat(atomicInteger->{
        
            String toId = String.valueOf(atomicInteger.getAndIncrement());
            
            String fromId = String.valueOf(atomicInteger.getAndIncrement());
    
            String addToCustomerRequest = Util.buildAddCustomerRequest(toId,
                                        Util.generateRandomString(), Util.generateRandomString());
            String addFromCustomerRequest = Util.buildAddCustomerRequest(fromId,
                                        Util.generateRandomString(), Util.generateRandomString());
            String addToAccountRequest = Util.buildAddAccountRequest(toId,
                                        "100", 
                                        "Account-"+toId,
                                        toId);
            String addFromAccountRequest = Util.buildAddAccountRequest(fromId,
                                        "100", 
                                        "Account-"+fromId,
                                        fromId);
            String moneyTransferRequest = Util.buildMoneyTransferRequest(toId, fromId, "20");
    
            customerServiceIT.addCustomer(addToCustomerRequest);
            customerServiceIT.addCustomer(addFromCustomerRequest);
            accountServiceIT.createAccount(addToAccountRequest);
            accountServiceIT.createAccount(addFromAccountRequest);

            Thread.sleep(500);

            moneyTransferServiceIT.createMoneyTransfer(moneyTransferRequest);

            Thread.sleep(500);

            AccountViewInfo toAccount = accountServiceIT.getAccount(toId);
            AccountViewInfo fromAccount = accountServiceIT.getAccount(fromId);
            
            boolean toAccountAdded = toAccount.getAccountInfo().getBalance().compareTo(new BigDecimal(120)) == 0; 
            boolean fromAccountDeducted = fromAccount.getAccountInfo().getBalance().compareTo(new BigDecimal(80)) == 0;

            return toAccountAdded && fromAccountDeducted;

          },new RunsInThreads<>(new AtomicInteger(), 10));
    }

    @After
    public void cleanUp(){

        moneyTransferServiceIT.destroy();
        customerServiceIT.destroy();
        accountServiceIT.destroy();
    }
}