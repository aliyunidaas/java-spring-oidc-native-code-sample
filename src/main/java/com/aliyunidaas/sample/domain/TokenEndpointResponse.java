package com.aliyunidaas.sample.domain;

import java.io.Serializable;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/15 3:28 PM
 * @author: yunqiu
 **/
public class TokenEndpointResponse implements Serializable {
    /**
     * token类型
     */
    private String tokenType;
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 身份令牌
     */
    private String idToken;
    /**
     * 过期时间
     */
    private String expiresIn;
    /**
     * 过期时间
     */
    private String expiresAt;

    public TokenEndpointResponse() {
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
