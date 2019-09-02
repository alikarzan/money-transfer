package com.ali.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import com.ali.MoneytransferApplication;
import com.ali.MoneytransferConfiguration;
import com.ali.account.web.AccountCreateRestResponse;
import com.ali.common.AccountViewInfo;
import com.ali.common.CustomerViewInfo;
import com.ali.integration.StateResponse;
import com.ali.integration.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.junit.After;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import io.dropwizard.testing.junit.DropwizardAppRule;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountServiceIT{

    @ClassRule
    public static final DropwizardAppRule<MoneytransferConfiguration> RULE =
            new DropwizardAppRule<>(MoneytransferApplication.class, "config.yml");

    private ObjectMapper mapper;
    private CloseableHttpClient client;
    private CustomerServiceIT customerServiceTest;

    private String createAccountRequest = "{\"id\":\"1\", \"balance\":\"200\",\"title\":\"Account 1\", \"owner\":\"1\"}";
    private String createOtherAccountRequest = "{\"id\":\"2\", \"balance\":\"100\",\"title\":\"Account 2\", \"owner\":\"2\"}";
    private String creteAccountWithoutOwnerRequest = "{\"id\":\"12\", \"balance\":\"200\",\"title\":\"Credit Account\"}";
    private String creteAccountWithoutBalanceRequest = "{\"id\":\"13\",\"title\":\"Credit Account\", \"owner\":\"1\"}";
    private String creteAccountWithInvalidOwnerRequest = "{\"id\":\"14\", \"balance\":\"200\",\"title\":\"Credit Account\", \"owner\":\"123\"}";
    private String createAccountWithNegativeBalacnceRequest = "{\"id\":\"15\", \"balance\":\"-200\",\"title\":\"Credit Account 2\", \"owner\":\"1234\"}";
    private String creteAccountWithouIdRequest = "{\"balance\":\"100\",\"title\":\"Account 2\", \"owner\":\"1\"}";

    private ErrorResponse errorResponse;

    private String invalidAccountId = "178";
    private String validAccountId = "1";
    private String validOwnerId = "1";

    public AccountServiceIT(){
        mapper = new ObjectMapper();
        client = HttpClientBuilder.create().build();
        customerServiceTest = new CustomerServiceIT();
    }

    @Test
    public void addAccountWithNullBody() throws ClientProtocolException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(null);
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(1, errorResponse.getErros().size());
        assertEquals("The request body must not be null", errorResponse.getErros().get(0));

    }

    @Test
    public void addAccountWithoutOwner() throws ClientProtocolException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(new StringEntity(creteAccountWithoutOwnerRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(1, errorResponse.getErros().size());
        assertEquals("owner must not be blank", errorResponse.getErros().get(0));

    }

    @Test
    public void addAccountWithoutBalance() throws ClientProtocolException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(new StringEntity(creteAccountWithoutBalanceRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(1, errorResponse.getErros().size());
        assertEquals("balance must not be null", errorResponse.getErros().get(0));

    }

    @Test
    public void addAccountWithoutId() throws ClientProtocolException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(new StringEntity(creteAccountWithouIdRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(1, errorResponse.getErros().size());
        assertEquals("id must not be blank", errorResponse.getErros().get(0));

    }

    @Test
    public void addAccountWithInvalidOwner() throws ClientProtocolException, IOException, InterruptedException{
        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(new StringEntity(creteAccountWithInvalidOwnerRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(202, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        AccountCreateRestResponse createAccountResponse = mapper.readValue(jsonResponse, AccountCreateRestResponse.class);
        String stateUri = createAccountResponse.getStateUri();

        // To compansate eventually consistency
        Thread.sleep(500);

        HttpGet getStateRequest = new HttpGet("http://localhost:9090"+stateUri);
        response = client.execute(getStateRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());

        jsonResponse = EntityUtils.toString(response.getEntity());
        StateResponse stateResponse = mapper.readValue(jsonResponse, StateResponse.class);

        assertEquals("FAIL", stateResponse.getState());
        assertEquals("Customer Given in Owner Does not Exists", stateResponse.getDescription());
        
    }

    @Test
    public void addAccountWithNegativeBalance() throws ClientProtocolException, IOException{
        

        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(new StringEntity(createAccountWithNegativeBalacnceRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(1, errorResponse.getErros().size());
        assertEquals("balance must be greater than 0", errorResponse.getErros().get(0));

    }

    @Test
    public void createAnotherAccount() throws ClientProtocolException, IOException, InterruptedException{

        createAnotherCustomer();
        Thread.sleep(1000);

        AccountCreateRestResponse createAccountResponse = createAccount(createOtherAccountRequest);
        assertEquals("2", createAccountResponse.getAccount().getAccountInfo().getId());
        assertEquals("2", createAccountResponse.getAccount().getAccountInfo().getOwner());
        assertEquals("Account 2", createAccountResponse.getAccount().getAccountInfo().getTitle());
        assertEquals(new BigDecimal(100), createAccountResponse.getAccount().getAccountInfo().getBalance());
        
    }


    @Test
    public void createAnAccount() throws ClientProtocolException, IOException, InterruptedException{
        
        createCustomer();
        Thread.sleep(1000);

        AccountCreateRestResponse createAccountResponse = createAccount(createAccountRequest);
        assertEquals("1", createAccountResponse.getAccount().getAccountInfo().getId());
        assertEquals("1", createAccountResponse.getAccount().getAccountInfo().getOwner());
        assertEquals("Account 1", createAccountResponse.getAccount().getAccountInfo().getTitle());
        assertEquals(new BigDecimal(200), createAccountResponse.getAccount().getAccountInfo().getBalance());
        
    }

    @Test
    public void getAccounts() throws ClientProtocolException, IOException{

        HttpGet getRequest = new HttpGet("http://localhost:9090/accounts");
        getRequest.setHeader("Accept", "application/json");
        
        CloseableHttpResponse response = client.execute(getRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());
    
        String jsonResponse = EntityUtils.toString(response.getEntity());

        Collection<AccountViewInfo> accounts = mapper.readValue(jsonResponse, new TypeReference<Collection<AccountViewInfo>>() {});

        assertEquals(2, accounts.size());

        Iterator<AccountViewInfo> accountIterator = accounts.iterator();
        AccountViewInfo accountViewInfo;

        accountViewInfo = accountIterator.next();

        assertEquals("1", accountViewInfo.getAccountInfo().getId());
        assertEquals("1", accountViewInfo.getAccountInfo().getOwner());
        assertEquals("Account 1", accountViewInfo.getAccountInfo().getTitle());
        assertEquals(new BigDecimal(200), accountViewInfo.getAccountInfo().getBalance());

        accountViewInfo = accountIterator.next();

        assertEquals("2", accountViewInfo.getAccountInfo().getId());
        assertEquals("2", accountViewInfo.getAccountInfo().getOwner());
        assertEquals("Account 2", accountViewInfo.getAccountInfo().getTitle());
        assertEquals(new BigDecimal(100), accountViewInfo.getAccountInfo().getBalance());

    }

    @Test
    public void getAccount() throws ClientProtocolException, IOException{
/*
        HttpGet getRequest = new HttpGet("http://localhost:9090/accounts/1");
        getRequest.setHeader("Accept", "application/json");

        CloseableHttpResponse response = client.execute(getRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        AccountViewInfo account = mapper.readValue(jsonResponse, AccountViewInfo.class);
*/
        AccountViewInfo account = getAccount("1");

        assertEquals("1", account.getAccountInfo().getId());
        assertEquals("1", account.getAccountInfo().getOwner());
        assertEquals("Account 1", account.getAccountInfo().getTitle());
        assertEquals(new BigDecimal(200), account.getAccountInfo().getBalance());
    }

    public AccountViewInfo getAccount(String accountId) throws ClientProtocolException, IOException{
        HttpGet getRequest = new HttpGet("http://localhost:9090/accounts/"+accountId);
        getRequest.setHeader("Accept", "application/json");

        CloseableHttpResponse response = client.execute(getRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        AccountViewInfo account = mapper.readValue(jsonResponse, AccountViewInfo.class);

        return account;
    }

    @Test
    public void getAccountFromCustomerView() throws ClientProtocolException, IOException{
        HttpGet getRequest = new HttpGet("http://localhost:9090/customers/"+validOwnerId);
        getRequest.setHeader("Accept", "application/json");
        CloseableHttpResponse response = client.execute(getRequest);
  
        assertEquals(200, response.getStatusLine().getStatusCode());
  
        String jsonResponse = EntityUtils.toString(response.getEntity());
  
        CustomerViewInfo customer = mapper.readValue(jsonResponse, CustomerViewInfo.class);
  
        assertEquals("Jack", customer.getCustomerInfo().getFirstName());
        assertEquals("1", customer.getCustomerInfo().getSsn());
        assertEquals("1", customer.getAccountInfo().get(0).getAccountInfo().getId());
        assertEquals(new BigDecimal(200), customer.getAccountInfo().get(0).getAccountInfo().getBalance());
    }

    @Test
    public void getAccountWithInvalidAccountId() throws ClientProtocolException, IOException{

        HttpGet getRequest = new HttpGet("http://localhost:9090/accounts/"+invalidAccountId);
        getRequest.setHeader("Accept", "application/json");

        CloseableHttpResponse response = client.execute(getRequest);

        assertEquals(204, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getAccountWithPost() throws ClientProtocolException, IOException{

      HttpPost getRequest = new HttpPost("http://localhost:9090/accounts/"+validAccountId);
      getRequest.setHeader("Accept", "application/json");
      CloseableHttpResponse response = client.execute(getRequest);

      assertEquals(405, response.getStatusLine().getStatusCode());

    }

    private void createCustomer() throws ParseException, IOException {
        customerServiceTest.addACustomer();
    }

    private void createAnotherCustomer() throws ParseException, IOException{
        customerServiceTest.addAnotherCustomer();
    }

    public AccountCreateRestResponse createAccount(String createAccountReq) throws ClientProtocolException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/account");
        postRequest.setEntity(new StringEntity(createAccountReq));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(202, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        AccountCreateRestResponse createAccountResponse = mapper.readValue(jsonResponse, AccountCreateRestResponse.class);

        return createAccountResponse;

    }

    @After
    public void cleanUp(){
        destroy();
    }

    public void destroy(){
        customerServiceTest.destroy();

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}