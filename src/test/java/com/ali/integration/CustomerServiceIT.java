package com.ali.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import com.ali.common.CustomerViewInfo;
import com.ali.customer.web.CustomerCreateRestResponse;
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
import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerServiceIT {
    
    static ObjectMapper mapper;
    static CloseableHttpClient client;
    
    private String creteCustomerRequest = "{\"firstName\":\"Jack2\", \"lastName\":\"McClouh2\",\"ssn\":\"2\"}";
    private CustomerCreateRestResponse createCustomerResponse;

   // @BeforeClass
    public static void initialize(){
      mapper = new ObjectMapper();
      client = HttpClientBuilder.create().build();
    }

   // @AfterClass
    public static void cleanUp(){
      try {
          client.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
    
    //@Test
    public void createCustomer() throws ParseException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/customer");
        postRequest.setEntity(new StringEntity(creteCustomerRequest));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(202, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        createCustomerResponse = mapper.readValue(jsonResponse, CustomerCreateRestResponse.class);

    }

  //@Test
    public void getCustomer() throws ClientProtocolException, IOException{

      HttpGet getRequest = new HttpGet("http://localhost:9090/customers/2");
      getRequest.setHeader("Accept", "application/json");
      //postRequest.setHeader("Content-type", "application/json");

      CloseableHttpResponse response = client.execute(getRequest);

      String jsonResponse = EntityUtils.toString(response.getEntity());

      CustomerViewInfo customer = mapper.readValue(jsonResponse, CustomerViewInfo.class);

      assertEquals(200, response.getStatusLine().getStatusCode());
      assertEquals("Jack2", customer.getCustomerInfo().getFirstName());

    }

    //@Test
    public void getAllCustomers()  throws ClientProtocolException, IOException{

      HttpGet getRequest = new HttpGet("http://localhost:9090/customers");

      getRequest.setHeader("Accept", "application/json");

      CloseableHttpResponse response = client.execute(getRequest);

      String jsonResponse = EntityUtils.toString(response.getEntity());

      Collection<CustomerViewInfo> customers = (Collection<CustomerViewInfo>)mapper.readValue(jsonResponse, Collection.class);

      assertEquals(200, response.getStatusLine().getStatusCode());
      assertEquals(customers.size(), 1);

    }
}