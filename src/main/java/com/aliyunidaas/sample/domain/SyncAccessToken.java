package com.aliyunidaas.sample.domain;

import java.io.Serializable;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/8 4:16 PM
 * @author: yunqiu
 **/
public class SyncAccessToken implements Serializable {

    private static final long serialVersionUID = 49368342525673345L;
    /**
     * 令牌
     */
    private String accessToken;
    /**
     * 过期时间,expiresIn秒后过期
     */
    private Long expiresIn;
    /**
     * 在expiresAt时过期,Unix时间戳，单位秒
     */
    private Long expiresAt;

    public SyncAccessToken() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public static SyncApiAccessTokenBuilder getBuilder() {
        return SyncApiAccessTokenBuilder.aSyncApiAccessToken();
    }

    public static final class SyncApiAccessTokenBuilder {
        private String accessToken;
        private Long expiresIn;
        private Long expiresAt;

        private SyncApiAccessTokenBuilder() {}

        public static SyncApiAccessTokenBuilder aSyncApiAccessToken() {return new SyncApiAccessTokenBuilder();}

        public SyncApiAccessTokenBuilder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public SyncApiAccessTokenBuilder setExpiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public SyncApiAccessTokenBuilder setExpiresAt(Long expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public SyncAccessToken build() {
            SyncAccessToken syncAccessToken = new SyncAccessToken();
            syncAccessToken.setAccessToken(accessToken);
            syncAccessToken.setExpiresIn(expiresIn);
            syncAccessToken.setExpiresAt(expiresAt);
            return syncAccessToken;
        }
    }
}
