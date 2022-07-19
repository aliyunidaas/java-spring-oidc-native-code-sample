package com.aliyunidaas.sample.common.config;

import com.aliyunidaas.sample.common.factory.CodeChallengeMethodFactory;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/23 7:14 PM
 * @author: longqiuling
 **/
@Configuration
@ConfigurationProperties("idaas.oidc")
public class CustomOidcConfiguration {

    private boolean openPkce = false;

    private String codeChallengeMethod = CodeChallengeMethodFactory.SHA_256;

    private String clientId;

    private String clientSecret;

    private String issuer;

    private String scopes = ParameterNameFactory.DEFAULT_SCOPES;

    private String redirectUri;

    public CustomOidcConfiguration() {
    }

    public boolean isOpenPkce() {
        return openPkce;
    }

    public void setOpenPkce(boolean openPkce) {
        this.openPkce = openPkce;
    }

    public String getCodeChallengeMethod() {
        return codeChallengeMethod;
    }

    public void setCodeChallengeMethod(String codeChallengeMethod) {
        this.codeChallengeMethod = codeChallengeMethod;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
