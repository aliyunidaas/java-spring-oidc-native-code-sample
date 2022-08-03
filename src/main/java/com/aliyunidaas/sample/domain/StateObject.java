package com.aliyunidaas.sample.domain;

import java.util.Map;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/20 11:42 AM
 * @author: yunqiu
 **/
public class StateObject {
    /**
     * 授权码
     */
    public String authorizationCode;
    /**
     * 访问令牌
     */
    public String accessToken;
    /**
     * 身份令牌(已经解析)
     */
    public Map<String, Object> idTokenClaimsMap;
    /**
     * 刷新令牌
     */
    public String refreshToken;
    /**
     * 用户信息
     */
    private UserInfoEndpointResponse userInfo;

    public StateObject() {
    }

    public UserInfoEndpointResponse getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoEndpointResponse userInfo) {
        this.userInfo = userInfo;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, Object> getIdTokenClaimsMap() {
        return idTokenClaimsMap;
    }

    public void setIdTokenClaimsMap(Map<String, Object> idTokenClaimsMap) {
        this.idTokenClaimsMap = idTokenClaimsMap;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static StateObjectBuilder getBuilder() {
        return StateObjectBuilder.aStateObject();
    }

    public static final class StateObjectBuilder {
        public String authorizationCode;
        public String accessToken;
        public Map<String, Object> idTokenClaimsMap;
        public String refreshToken;
        private UserInfoEndpointResponse userInfo;

        private StateObjectBuilder() {}

        public static StateObjectBuilder aStateObject() {return new StateObjectBuilder();}

        public StateObjectBuilder setAuthorizationCode(String authorizationCode) {
            this.authorizationCode = authorizationCode;
            return this;
        }

        public StateObjectBuilder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public StateObjectBuilder setIdTokenClaimsMap(Map<String, Object> idTokenClaimsMap) {
            this.idTokenClaimsMap = idTokenClaimsMap;
            return this;
        }

        public StateObjectBuilder setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public StateObjectBuilder setUserInfo(UserInfoEndpointResponse userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        public StateObject build() {
            StateObject stateObject = new StateObject();
            stateObject.setAuthorizationCode(authorizationCode);
            stateObject.setAccessToken(accessToken);
            stateObject.setIdTokenClaimsMap(idTokenClaimsMap);
            stateObject.setRefreshToken(refreshToken);
            stateObject.setUserInfo(userInfo);
            return stateObject;
        }
    }
}
