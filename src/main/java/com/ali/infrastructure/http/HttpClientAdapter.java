package com.ali.infrastructure.http;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientAdapter {

    
    public String getResource(String resourceUri) throws ParseException, IOException {

    CloseableHttpClient client = HttpClientBuilder.create().build();

    HttpGet getRequest = new HttpGet(resourceUri);
    getRequest.setHeader("Accept", "application/json");
    
    CloseableHttpResponse response =  client.execute(getRequest);
    
    if(response.getStatusLine().getStatusCode() == 404 || response.getStatusLine().getStatusCode() == 204){
        return null;
    }
    String jsonResponse = EntityUtils.toString(response.getEntity());  
    client.close();

    return jsonResponse;
    }



}