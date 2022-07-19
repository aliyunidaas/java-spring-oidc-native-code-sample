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

    public EndpointContext() {
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }

    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }

    public static EndpointContextBuilder getBuilder() {
        return EndpointContextBuilder.anEndpointContext();
    }

    public static final class EndpointContextBuilder {
        private String authorizationEndpoint;
        private String tokenEndpoint;
        private String userinfoEndpoint;

        private EndpointContextBuilder() {}

        public static EndpointContextBuilder anEndpointContext() {return new EndpointContextBuilder();}

        public EndpointContextBuilder setAuthorizationEndpoint(String authorizationEndpoint) {
            this.authorizationEndpoint = authorizationEndpoint;
            return this;
        }

        public EndpointContextBuilder setTokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
            return this;
        }

        public EndpointContextBuilder setUserinfoEndpoint(String userinfoEndpoint) {
            this.userinfoEndpoint = userinfoEndpoint;
            return this;
        }

        public EndpointContext build() {
            EndpointContext endpointContext = new EndpointContext();
            endpointContext.setAuthorizationEndpoint(authorizationEndpoint);
            endpointContext.setTokenEndpoint(tokenEndpoint);
            endpointContext.setUserinfoEndpoint(userinfoEndpoint);
            return endpointContext;
        }
    }
}

