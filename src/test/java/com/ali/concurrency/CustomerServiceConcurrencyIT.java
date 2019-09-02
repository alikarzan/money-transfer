package com.ali.concurrency;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.ali.MoneytransferApplication;
import com.ali.MoneytransferConfiguration;
import com.ali.common.CustomerViewInfo;
import com.ali.integration.CustomerServiceIT;

import org.apache.http.ParseException;
import org.cactoos.matchers.RunsInThreads;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;


import io.dropwizard.testing.junit.DropwizardAppRule;

public class CustomerServiceConcurrencyIT{

    @ClassRule
    public static final DropwizardAppRule<MoneytransferConfiguration> RULE =
            new DropwizardAppRule<>(MoneytransferApplication.class, "config.yml");

    private CustomerServiceIT customerServiceITest;

    @Before
    public void initialize(){
        customerServiceITest = new CustomerServiceIT();
    }

    @Test
    public void testConcurrentCallsToCreateCustomer() throws ParseException, IOException{
      MatcherAssert.assertThat(atomicInteger->{
        
        String customerId = String.valueOf(atomicInteger.getAndIncrement());

        String addCustomerRequest = Util.buildAddCustomerRequest(customerId,
                                    Util.generateRandomString(), Util.generateRandomString());

        customerServiceITest.addCustomer(addCustomerRequest);
        Thread.sleep(500);
        CustomerViewInfo customer = customerServiceITest.getCustomer(customerId);

        return customer.getCustomerInfo().getSsn().equalsIgnoreCase(customerId);

      },new RunsInThreads<>(new AtomicInteger(), 10));

    }

    @After
    public void cleanup(){
      customerServiceITest.destroy();
    }
    
}