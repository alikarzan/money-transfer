package com.ali.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

import com.ali.MoneytransferApplication;
import com.ali.MoneytransferConfiguration;
import com.ali.common.AccountViewInfo;
import com.ali.integration.AccountServiceIT;
import com.ali.integration.CustomerServiceIT;

import org.cactoos.matchers.RunsInThreads;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import io.dropwizard.testing.junit.DropwizardAppRule;

public class AccountServiceConcurrencyIT {

    @ClassRule
    public static final DropwizardAppRule<MoneytransferConfiguration> RULE =
            new DropwizardAppRule<>(MoneytransferApplication.class, "config.yml");

    private AccountServiceIT accountServiceITest;
    private CustomerServiceIT customerServiceITest;

    @Before
    public void initialize(){
        accountServiceITest = new AccountServiceIT();
        customerServiceITest = new CustomerServiceIT();
    }

    @Test
    public void testConcurrentCallsToCreateAccount(){
        MatcherAssert.assertThat(atomicInteger->{
        
            String id = String.valueOf(atomicInteger.getAndIncrement());

            String addCustomerRequest = Util.buildAddCustomerRequest(id,
                                    Util.generateRandomString(), Util.generateRandomString());

            customerServiceITest.addCustomer(addCustomerRequest);

            Thread.sleep(500);
    
            String addAccountRequest = Util.buildAddAccountRequest(id,
                                        "100", 
                                        "Account-"+id,
                                        id);
    
            accountServiceITest.createAccount(addAccountRequest);

            Thread.sleep(500);
            
            AccountViewInfo account = accountServiceITest.getAccount(id);
    
            return account.getAccountInfo().getId().equalsIgnoreCase(id);
    
          },new RunsInThreads<>(new AtomicInteger(), 10));
    }

    @After
    public void cleanup(){
        accountServiceITest.destroy();
        customerServiceITest.destroy();
    }
}