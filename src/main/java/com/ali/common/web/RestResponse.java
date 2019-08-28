package com.ali.common.web;

public class RestResponse{
    private String stateUri;
    private String resourceUri;

    public RestResponse(String stateUri, String resourceUri){
        this.stateUri = stateUri;
        this.resourceUri = resourceUri;
    }

    public String getStateUri(){
        return this.stateUri;
    }
    public String getResourceUri(){
        return this.resourceUri;
    }
}