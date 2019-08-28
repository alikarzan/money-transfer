package com.ali.common.web;

import javax.ws.rs.core.Response;

public class RestUtil{

    public static final String STATE_URI = "/state/";
    public static final String RESOURCE_URI_CUSTOMER = "/customers/";
    public static final String RESOURCE_URI_ACCOUNT = "/accounts/";

    public static Response generateAcceptedResponse(RestResponse response){
        return Response.status(Response.Status.ACCEPTED).entity(response).build();
    }

    public static Response generateServerErrorResponse(String errorMessage){
        return Response.serverError().entity(errorMessage).build();
    }
}