package com.ali.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;

import com.ali.account.web.AccountCreateRestResponse;
import com.ali.common.AccountViewInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccountServiceIT{

    static ObjectMapper mapper;
    static CloseableHttpClient client;

    private String creteAccountRequest = "{\"id\":\"1\", \"balance\":\"200\",\"title\":\"Credit Account\", \"owner\":\"2\"}";

    //@Test
    public void createAccount() throws ClientProtocolException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(new StringEntity(creteAccountRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(202, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        AccountCreateRestResponse createAccountResponse = mapper.readValue(jsonResponse, AccountCreateRestResponse.class);

        System.out.println(createAccountResponse.getAccount().getAccountInfo().getId());

    }

    //@Test
    public void getAccounts() throws ClientProtocolException, IOException{

        HttpGet getRequest = new HttpGet("http://localhost:9090/accounts");
        getRequest.setHeader("Accept", "application/json");
        
        CloseableHttpResponse response = client.execute(getRequest);
    
        String jsonResponse = EntityUtils.toString(response.getEntity());

        Collection<AccountViewInfo> accounts = (Collection<AccountViewInfo>)mapper.readValue(jsonResponse, Collection.class);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(accounts.size(), 1);

    }

    //@Test
    public void getAccount() throws ClientProtocolException, IOException{

        HttpGet getRequest = new HttpGet("http://localhost:9090/accounts/1");
        getRequest.setHeader("Accept", "application/json");

        CloseableHttpResponse response = client.execute(getRequest);

        String jsonResponse = EntityUtils.toString(response.getEntity());

        AccountViewInfo account = mapper.readValue(jsonResponse, AccountViewInfo.class);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(new BigDecimal(200), account.getAccountInfo().getBalance());
    }
}