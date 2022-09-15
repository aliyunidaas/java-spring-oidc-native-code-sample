package com.aliyunidaas.sample.common;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/5 1:57 PM
 * @author: longqiuling
 **/
public class EndpointContext {

    private String authorizationEndpoint;

    private String tokenEndpoint;

    private String userinfoEndpoint;

    private String jwksUri;

    public EndpointContext() {
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }
}

