package com.aliyunidaas.sample.common.config;

import com.aliyunidaas.sample.common.factory.CodeChallengeMethodFactory;
import com.aliyunidaas.sample.common.factory.ConstantParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/28 3:52 PM
 * @author: yunqiu
 **/
@Configuration
@ConfigurationProperties("idaas")
public class InitConfiguration {

    private String instanceId;

    private String applicationId;

    private OidcConfig oidcConfig;

    private SyncConfig syncConfig;

    public InitConfiguration() {
    }

    @PostConstruct
    private void init() throws Exception {
        if (StringUtils.isBlank(oidcConfig.getClientId())) {
            throw new Exception(ConstantParams.CLIENT_ID_IS_NULL);
        }
        if (StringUtils.isBlank(oidcConfig.getClientSecret())) {
            throw new Exception(ConstantParams.CLIENT_SECRET_IS_NULL);
        }
        if (StringUtils.isBlank(oidcConfig.getIssuer())) {
            throw new Exception(ConstantParams.ISSUER_IS_NULL);
        }
        if (StringUtils.isBlank(oidcConfig.getRedirectUri())) {
            throw new Exception(ConstantParams.REDIRECT_URI_IS_NULL);
        }
        validUri(oidcConfig.getRedirectUri(), ConstantParams.REDIRECT_URI_IS_VALID);

        if (StringUtils.isBlank(syncConfig.getCallbackUri())) {
            throw new Exception(ConstantParams.CALLBACK_URI_IS_NULL);
        }
        validUri(syncConfig.getCallbackUri(), ConstantParams.CALLBACK_URI_IS_VALID);

        if (syncConfig.isEncryptRequired() && StringUtils.isBlank(syncConfig.getEncryptKey())) {
            throw new Exception(ConstantParams.ENCRYPT_KEY_IS_NULL);
        }
        if (StringUtils.isBlank(syncConfig.getJwksUri())) {
            throw new Exception(ConstantParams.JWKS_URI_IS_NULL);
        }
        validUri(syncConfig.getJwksUri(), ConstantParams.JWKS_URI_IS_VALID);
    }

    private void validUri(String redirectUri, String message) throws Exception {
        try {
            new URI(redirectUri);
        } catch (URISyntaxException e) {
            throw new Exception(message);
        }
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public OidcConfig getOidcConfig() {
        return oidcConfig;
    }

    public void setOidcConfig(OidcConfig oidcConfig) {
        this.oidcConfig = oidcConfig;
    }

    public SyncConfig getSyncConfig() {
        return syncConfig;
    }

    public void setSyncConfig(SyncConfig syncConfig) {
        this.syncConfig = syncConfig;
    }

    public static class SyncConfig {

        private boolean enabled = true;

        private boolean encryptRequired = true;

        private String encryptKey;

        private String callbackUri;

        private String jwksUri;

        public SyncConfig() {
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEncryptRequired() {
            return encryptRequired;
        }

        public void setEncryptRequired(boolean encryptRequired) {
            this.encryptRequired = encryptRequired;
        }

        public String getEncryptKey() {
            return encryptKey;
        }

        public void setEncryptKey(String encryptKey) {
            this.encryptKey = encryptKey;
        }

        public String getCallbackUri() {
            return callbackUri;
        }

        public void setCallbackUri(String callbackUri) {
            this.callbackUri = callbackUri;
        }

        public String getJwksUri() {
            return jwksUri;
        }

        public void setJwksUri(String jwksUri) {
            this.jwksUri = jwksUri;
        }
    }

    public static class OidcConfig {

        private boolean pkceRequired = false;

        private String codeChallengeMethod = CodeChallengeMethodFactory.SHA_256;

        private String clientId;

        private String clientSecret;

        private String issuer;

        private String scopes = ConstantParams.DEFAULT_SCOPES;

        private String redirectUri;

        public OidcConfig() {
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
}
