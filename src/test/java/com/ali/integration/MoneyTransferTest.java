package com.ali.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

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

public class MoneyTransferTest{

    static ObjectMapper mapper;
    static CloseableHttpClient client;

    private String creteTransferRequest = "{\"toAccountId\":\"1\", \"fromAccountId\":\"2\",\"amount\":\"50\"}";
    private BigDecimal fromAmountInitialBalance = new BigDecimal(200);
    private BigDecimal toAmountInitialBalance = new BigDecimal(100);
    private BigDecimal transferAmount = new BigDecimal(50);

    //@BeforeClass
    public static void initialize(){
      mapper = new ObjectMapper();
      client = HttpClientBuilder.create().build();
    }

    //@AfterClass
    public static void cleanUp(){
      try {
          client.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
    }

    //@Test
    public void createMoneyTransfer() throws ClientProtocolException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/transfer");
        postRequest.setEntity(new StringEntity(creteTransferRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(202, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        MoneyTransferCreateRestResponse createAccountResponse = mapper.readValue(jsonResponse, MoneyTransferCreateRestResponse.class);

    }

    //@Test
    public void checkFromAccountAfterTransfer() throws ClientProtocolException, IOException{
        AccountViewInfo account = getAccount("1"); 
        assertEquals(fromAmountInitialBalance.subtract(transferAmount), account.getAccountInfo().getBalance());

    }

    //@Test
    public void checkToAccountAfterTransfer() throws ClientProtocolException, IOException{


        AccountViewInfo account = getAccount("2"); 
        assertEquals(toAmountInitialBalance.add(transferAmount), account.getAccountInfo().getBalance());

    }

    private AccountViewInfo getAccount(String accountId) throws IOException{
        HttpGet getRequest = new HttpGet("http://localhost:9090/accounts/"+accountId);
        getRequest.setHeader("Accept", "application/json");
        
        CloseableHttpResponse response = client.execute(getRequest);
        
        String jsonResponse = EntityUtils.toString(response.getEntity());

        AccountViewInfo account = mapper.readValue(jsonResponse, AccountViewInfo.class);

        return account;
    }

}