package com.aliyunidaas.sample.common.config;

import com.aliyunidaas.sample.common.factory.CodeChallengeMethodFactory;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

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

    private boolean pkceRequired = false;

    private String codeChallengeMethod = CodeChallengeMethodFactory.SHA_256;

    private String clientId;

    private String clientSecret;

    private String issuer;

    private String scopes = ParameterNameFactory.DEFAULT_SCOPES;

    private String redirectUri;

    public CustomOidcConfiguration() {
    }

    @PostConstruct
    private void valid() throws Exception {
        if (StringUtils.isBlank(clientId)) {
            throw new Exception(ParameterNameFactory.CLIENT_ID_IS_NULL);
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new Exception(ParameterNameFactory.CLIENT_SECRET_IS_NULL);
        }
        if (StringUtils.isBlank(issuer)) {
            throw new Exception(ParameterNameFactory.ISSUER_IS_NULL);
        }
        if (StringUtils.isBlank(redirectUri)) {
            throw new Exception(ParameterNameFactory.REDIRECT_URI_IS_NULL);
        }
        validRedirectUri();
    }

    private void validRedirectUri() throws Exception {
        URI uri = null;
        try {
            uri = new URI(redirectUri);
        } catch (URISyntaxException e) {
            throw new Exception(ParameterNameFactory.REDIRECT_URI_IS_VALID);
        }
        if (uri.getHost() == null) {
            throw new Exception(ParameterNameFactory.REDIRECT_URI_IS_VALID);
        }
    }

    public boolean isPkceRequired() {
        return pkceRequired;
    }

    public void setPkceRequired(boolean pkceRequired) {
        this.pkceRequired = pkceRequired;
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
