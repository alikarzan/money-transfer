package com.ali.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import com.ali.MoneytransferApplication;
import com.ali.MoneytransferConfiguration;
import com.ali.common.CustomerViewInfo;
import com.ali.customer.web.CustomerCreateRestResponse;
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
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import io.dropwizard.testing.junit.DropwizardAppRule;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerServiceIT {

  @ClassRule
  public static final DropwizardAppRule<MoneytransferConfiguration> RULE =
          new DropwizardAppRule<>(MoneytransferApplication.class, "config.yml");
    
    private ObjectMapper mapper;
    private CloseableHttpClient client;

    private ErrorResponse errorResponse;
    
    private final String createCustomerRequest = "{\"firstName\":\"Jack\", \"lastName\":\"McClough\",\"ssn\":\"1\"}";
    private final String createOtherCustomerRequest = "{\"firstName\":\"John\", \"lastName\":\"Louise\",\"ssn\":\"2\"}";
    private final String createCustomerRequestWithoutSsn = "{\"firstName\":\"Jack\", \"lastName\":\"McClough\"}";
    private final String createCustomerRequestWithoutFirstName = "{\"lastName\":\"McClough\", \"ssn\":\"1\"}";
    private final String createCustomerRequestEmpty = "{}";

    private String invalidCustomerSsn = "123";

    public CustomerServiceIT(){
      mapper = new ObjectMapper();
      client = HttpClientBuilder.create().build();
    }
    

    @Test
    public void addCustomerWithNullBody() throws ParseException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/customer");
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
    public void addCustomerWithEmptyCustomerInfo() throws ParseException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/customer");
        postRequest.setEntity(new StringEntity(createCustomerRequestEmpty));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(3, errorResponse.getErros().size());
      
    }

    @Test
    public void addCustomerWithoutSsn() throws ParseException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/customer");
        postRequest.setEntity(new StringEntity(createCustomerRequestWithoutSsn));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(1, errorResponse.getErros().size());
        assertEquals("ssn must not be blank", errorResponse.getErros().get(0));

    }

    @Test
    public void addCustomerWithoutFirstName() throws ParseException, IOException{

        HttpPost postRequest = new HttpPost("http://localhost:9090/customer");
        postRequest.setEntity(new StringEntity(createCustomerRequestWithoutFirstName));
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(postRequest);

        assertEquals(422, response.getStatusLine().getStatusCode());

        String jsonResponse = EntityUtils.toString(response.getEntity());

        errorResponse = mapper.readValue(jsonResponse, ErrorResponse.class);

        assertEquals(1, errorResponse.getErros().size());
        assertEquals("firstName must not be blank", errorResponse.getErros().get(0));

    }
    
    @Test
    public void addACustomer() throws ParseException, IOException{

        CustomerCreateRestResponse createCustomerResponse = addCustomer(createCustomerRequest);

        assertEquals("Jack", createCustomerResponse.getCustomer().getCustomerInfo().getFirstName());
        assertEquals("McClough", createCustomerResponse.getCustomer().getCustomerInfo().getLastName());
        assertEquals("1", createCustomerResponse.getCustomer().getCustomerInfo().getSsn());

        assertEquals("/customers/1", createCustomerResponse.getResourceUri());
        assertTrue(createCustomerResponse.getStateUri().startsWith("/state/"));
    }

    @Test
    public void addAnotherCustomer() throws ParseException, IOException{

      CustomerCreateRestResponse createCustomerResponse = addCustomer(createOtherCustomerRequest);

      assertEquals("John", createCustomerResponse.getCustomer().getCustomerInfo().getFirstName());
      assertEquals("Louise", createCustomerResponse.getCustomer().getCustomerInfo().getLastName());
      assertEquals("2", createCustomerResponse.getCustomer().getCustomerInfo().getSsn());

      assertEquals("/customers/2", createCustomerResponse.getResourceUri());
      assertTrue(createCustomerResponse.getStateUri().startsWith("/state/"));
    }

    public CustomerCreateRestResponse addCustomer(String createCustomerReq) throws ParseException, IOException{
      HttpPost postRequest = new HttpPost("http://localhost:9090/customer");
      postRequest.setEntity(new StringEntity(createCustomerReq));
      postRequest.setHeader("Accept", "application/json");
      postRequest.setHeader("Content-type", "application/json");

      CloseableHttpResponse response = client.execute(postRequest);

      assertEquals(202, response.getStatusLine().getStatusCode());

      String jsonResponse = EntityUtils.toString(response.getEntity());

      CustomerCreateRestResponse createCustomerResponse = mapper.readValue(jsonResponse, CustomerCreateRestResponse.class);

      return createCustomerResponse;
    }

  @Test
    public void getCustomer() throws ClientProtocolException, IOException{

      CustomerViewInfo customer = getCustomer("1");
      assertEquals("Jack", customer.getCustomerInfo().getFirstName());

    }

    public CustomerViewInfo getCustomer(String customerId) throws ClientProtocolException, IOException{
      HttpGet getRequest = new HttpGet("http://localhost:9090/customers/"+customerId);
      getRequest.setHeader("Accept", "application/json");
      CloseableHttpResponse response = client.execute(getRequest);

      assertEquals(200, response.getStatusLine().getStatusCode());

      String jsonResponse = EntityUtils.toString(response.getEntity());

      CustomerViewInfo customer = mapper.readValue(jsonResponse, CustomerViewInfo.class);

      return customer;
    }

    @Test
    public void getCustomerWithInvalidSsn() throws ClientProtocolException, IOException{

      HttpGet getRequest = new HttpGet("http://localhost:9090/customers/"+invalidCustomerSsn);
      getRequest.setHeader("Accept", "application/json");
      CloseableHttpResponse response = client.execute(getRequest);

      assertEquals(204, response.getStatusLine().getStatusCode());

    }

    @Test
    public void getCustomerWithPost() throws ClientProtocolException, IOException{

      HttpPost getRequest = new HttpPost("http://localhost:9090/customers/"+invalidCustomerSsn);
      getRequest.setHeader("Accept", "application/json");
      CloseableHttpResponse response = client.execute(getRequest);

      assertEquals(405, response.getStatusLine().getStatusCode());

    }

    @Test
    public void getAllCustomers()  throws ClientProtocolException, IOException{

      HttpGet getRequest = new HttpGet("http://localhost:9090/customers");

      getRequest.setHeader("Accept", "application/json");

      CloseableHttpResponse response = client.execute(getRequest);
   
      assertEquals(200, response.getStatusLine().getStatusCode());

      String jsonResponse = EntityUtils.toString(response.getEntity());

      Collection<CustomerViewInfo> customers = mapper.readValue(jsonResponse, new TypeReference<Collection<CustomerViewInfo>>() {});
      
      assertEquals(2, customers.size());

      Iterator<CustomerViewInfo> customerIterator = customers.iterator();

      CustomerViewInfo customerInfo = customerIterator.next();
      
      assertEquals("1", customerInfo.getCustomerInfo().getSsn());
      assertEquals("Jack", customerInfo.getCustomerInfo().getFirstName());
      assertEquals("McClough", customerInfo.getCustomerInfo().getLastName());
      assertNull(customerInfo.getAccountInfo());

      customerInfo = customerIterator.next();

      assertEquals("2", customerInfo.getCustomerInfo().getSsn());
      assertEquals("John", customerInfo.getCustomerInfo().getFirstName());
      assertEquals("Louise", customerInfo.getCustomerInfo().getLastName());
      assertNull(customerInfo.getAccountInfo());

    }

    @After
    public void cleanUp(){
      destroy();
    }

    public void destroy(){
      try {
        client.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}