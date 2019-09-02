package com.ali.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import com.ali.MoneytransferApplication;
import com.ali.MoneytransferConfiguration;
import com.ali.common.AccountViewInfo;
import com.ali.money_transfer.web.MoneyTransferCreateRestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.dropwizard.testing.junit.DropwizardAppRule;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MoneyTransferServiceIT{

    @ClassRule
    public static final DropwizardAppRule<MoneytransferConfiguration> RULE =
            new DropwizardAppRule<>(MoneytransferApplication.class, "config.yml");

    private ObjectMapper mapper;
    private CloseableHttpClient client;
    private AccountServiceIT accountService;
    private ErrorResponse errorResponse;

    private String createTransferRequest = "{\"toAccountId\":\"1\", \"fromAccountId\":\"2\",\"amount\":\"50\"}";
    private String createTransferFromInvalidAccount = "{\"toAccountId\":\"1\", \"fromAccountId\":\"200\",\"amount\":\"50\"}";
    private String createTransferToInvalidAccount = "{\"toAccountId\":\"100\", \"fromAccountId\":\"2\",\"amount\":\"50\"}";
    private String createTransferFromInvalidAccountToInvalidAccount = "{\"toAccountId\":\"100\", \"fromAccountId\":\"200\",\"amount\":\"50\"}";

    private String createTransferWithoutToAccountRequest = "{\"fromAccountId\":\"2\",\"amount\":\"50\"}";
    private String createTransferWithoutFromAccountRequest = "{\"toAccountId\":\"1\", \"amount\":\"50\"}";
    private String createTransferWithoutTransferAmountRequest = "{\"toAccountId\":\"1\", \"fromAccountId\":\"2\"}";
    private String createTransferWithNegativeAmountRequest = "{\"toAccountId\":\"1\", \"fromAccountId\":\"2\",\"amount\":\"-50\"}";
    
    private BigDecimal fromAmountInitialBalance = new BigDecimal(100);
    private BigDecimal toAmountInitialBalance = new BigDecimal(200);
    private BigDecimal transferAmount = new BigDecimal(50);

    private String invalidFromAccount = "200";
    private String invalidToAccount = "100";

    public MoneyTransferServiceIT(){
        mapper = new ObjectMapper();
        client = HttpClientBuilder.create().build();
        accountService = new AccountServiceIT();
    }

 /*   @Before
    public void initialize(){
      mapper = new ObjectMapper();
      client = HttpClientBuilder.create().build();
      accountService = new AccountServiceIT();
    }
    */

    @Test
    public void createTransferWithNullBody() throws ClientProtocolException, IOException{
        HttpPost postRequest = new HttpPost("http://localhost:9090/transfer");
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
    public void createTransfferWithoutTransferAmount() throws ClientProtocolException, IOException{
        errorResponse = createErrornousTransfer(createTransferWithoutTransferAmountRequest);       
        assertEquals(1, errorResponse.getErros().size());
        assertEquals("amount must not be null", errorResponse.getErros().get(0));
    }

    @Test
    public void createTransfferWithoutFromAccount() throws ClientProtocolException, IOException{
        errorResponse = createErrornousTransfer(createTransferWithoutFromAccountRequest);
        assertEquals(1, errorResponse.getErros().size());
        assertEquals("fromAccountId must not be blank", errorResponse.getErros().get(0));
    }

    @Test
    public void createTransfferWithoutToAccount() throws ClientProtocolException, IOException{
        errorResponse = createErrornousTransfer(createTransferWithoutToAccountRequest);
        assertEquals(1, errorResponse.getErros().size());
        assertEquals("toAccountId must not be blank", errorResponse.getErros().get(0));
    }

    @Test
    public void createTransfferWithNegativeTransferAmount() throws ClientProtocolException, IOException{
        errorResponse = createErrornousTransfer(createTransferWithNegativeAmountRequest);
        assertEquals(1, errorResponse.getErros().size());
        assertEquals("amount must be greater than 0", errorResponse.getErros().get(0));
    }


    @Test
    public void createMoneyTransfer() throws ClientProtocolException, IOException, InterruptedException{

        accountService.createAnAccount();
        accountService.createAnotherAccount();
              
        MoneyTransferCreateRestResponse createAccountResponse = createMoneyTransfer(createTransferRequest);
        
        String stateUri = createAccountResponse.getStateUri();
        String toAccountUri = createAccountResponse.getResourceUri();
        String fromAccountUri = createAccountResponse.getResourceUri_2();

        HttpGet getRequest = new HttpGet("http://localhost:9090"+stateUri);
        getRequest.setHeader("Accept", "application/json");
        CloseableHttpResponse response = client.execute(getRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());
        StateResponse state = mapper.readValue(jsonResponse, StateResponse.class);

        assertEquals("SUCCESS", state.getState());

        getRequest = new HttpGet("http://localhost:9090"+toAccountUri);
        getRequest.setHeader("Accept", "application/json");
        response = client.execute(getRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());

        jsonResponse = EntityUtils.toString(response.getEntity());
        AccountViewInfo toAccountresponse = mapper.readValue(jsonResponse, AccountViewInfo.class);

        assertEquals("1", toAccountresponse.getAccountInfo().getId());
        assertEquals(toAmountInitialBalance.add(transferAmount), toAccountresponse.getAccountInfo().getBalance());

        getRequest = new HttpGet("http://localhost:9090"+fromAccountUri);
        getRequest.setHeader("Accept", "application/json");
        response = client.execute(getRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());

        jsonResponse = EntityUtils.toString(response.getEntity());
        AccountViewInfo fromAccountresponse = mapper.readValue(jsonResponse, AccountViewInfo.class);

        assertEquals("2", fromAccountresponse.getAccountInfo().getId());
        assertEquals(fromAmountInitialBalance.subtract(transferAmount), fromAccountresponse.getAccountInfo().getBalance());
    }

    public MoneyTransferCreateRestResponse createMoneyTransfer(String moneyTransferRequest) throws ClientProtocolException, IOException{
        HttpPost postRequest = new HttpPost("http://localhost:9090/transfer");
        postRequest.setEntity(new StringEntity(moneyTransferRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(202, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        MoneyTransferCreateRestResponse createAccountResponse = mapper.readValue(jsonResponse, MoneyTransferCreateRestResponse.class);

        return createAccountResponse;
    }

    @Test
    public void createMoneyTransferFromInvalidAccount() throws ClientProtocolException, IOException{

        StateResponse state = createMoneyTranserAndGetState(createTransferFromInvalidAccount);
        assertEquals("FAIL", state.getState());
        assertEquals("Invalid account, no such account as : fromAccount: "+invalidFromAccount, state.getDescription());

    }

    
    @Test
    public void createMoneyTransferToInvalidAccount() throws ClientProtocolException, IOException{

        StateResponse state = createMoneyTranserAndGetState(createTransferToInvalidAccount);
        assertEquals("FAIL", state.getState());
        assertEquals("Invalid account, no such account as : toAccount: "+invalidToAccount, state.getDescription());

    }

    @Test
    public void createMoneyTransferFromInvalidAccountToInvalidAccount() throws ClientProtocolException, IOException{
        StateResponse state = createMoneyTranserAndGetState(createTransferFromInvalidAccountToInvalidAccount);
        assertEquals("FAIL", state.getState());

        StringBuilder stateDescrfiptionBuilder = new StringBuilder();
        stateDescrfiptionBuilder.append("Invalid account, no such account as : ");
        stateDescrfiptionBuilder.append("toAccount: "+invalidToAccount);
        stateDescrfiptionBuilder.append(" and ");
        stateDescrfiptionBuilder.append("fromAccount: "+invalidFromAccount);

        assertEquals(stateDescrfiptionBuilder.toString(), state.getDescription());
    }

    private StateResponse createMoneyTranserAndGetState(String moneyTransferRequest) throws ClientProtocolException, IOException{
        HttpPost postRequest = new HttpPost("http://localhost:9090/transfer");
        postRequest.setEntity(new StringEntity(moneyTransferRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(202, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        MoneyTransferCreateRestResponse createAccountResponse = mapper.readValue(jsonResponse, MoneyTransferCreateRestResponse.class);
        String stateUri = createAccountResponse.getStateUri();

        HttpGet getRequest = new HttpGet("http://localhost:9090"+stateUri);
        postRequest.setHeader("Accept", "application/json");
        response = client.execute(getRequest);

        assertEquals(200, response.getStatusLine().getStatusCode());

        jsonResponse = EntityUtils.toString(response.getEntity());
        StateResponse state = mapper.readValue(jsonResponse, StateResponse.class);

        return state;
    }

    private ErrorResponse createErrornousTransfer(String errornousTransferRequest) throws ClientProtocolException, IOException{
        HttpPost postRequest = new HttpPost("http://localhost:9090/transfer");
        postRequest.setEntity(new StringEntity(errornousTransferRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        return errorResponse;
    }

    @After
    public void cleanUp(){
        destroy();
    }

    public void destroy(){
        accountService.destroy();

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}