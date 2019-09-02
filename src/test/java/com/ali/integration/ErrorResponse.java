package com.ali.integration;

import java.util.List;

public class ErrorResponse {
    private List<String> errors;

    public List<String> getErros(){
        return this.errors;
    }

    public void setErrors(List<String> errors){
        this.errors = errors;
    }
}