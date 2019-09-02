package com.ali.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateResponse {

    private String id;
    private String state;
    private String description;

    public String getState(){
        return this.state;
    }

    public void setState(String state){
        this.state = state;
    }
    public String getDescription(){
        return this.description;
    }
    public void setSate(String state){
        this.state = state;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
}