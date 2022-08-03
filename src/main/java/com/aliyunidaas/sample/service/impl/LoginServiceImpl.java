package com.aliyunidaas.sample.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.common.util.CommonUtil;
import com.aliyunidaas.sample.common.util.HttpConnectUtil;
import com.aliyunidaas.sample.domain.TokenEndpointResponse;
import com.aliyunidaas.sample.service.LoginService;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/27 11:25 AM
 * @author: longqiuling
 **/
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    @Override
    public TokenEndpointResponse swapCodeFromTokenEndpoint(String authenticationCode, String state) throws IOException {
        EndpointContext endpointContext = cacheManager.getCache(customOidcConfiguration.getIssuer());
        final String tokenEndpoint = endpointContext.getTokenEndpoint();
        final String clientId = customOidcConfiguration.getClientId();
        final String clientSecret = customOidcConfiguration.getClientSecret();
        final String redirectUri = customOidcConfiguration.getRedirectUri();
        final String scopes = customOidcConfiguration.getScopes().replace(" ", "%20");

        String basicAuthString = clientId + ":" + clientSecret;
        final String basicAuthHeader = Base64.getEncoder().encodeToString(basicAuthString.getBytes(StandardCharsets.UTF_8));

        List<NameValuePair> nameValues = new ArrayList<>();
        nameValues.add(new BasicNameValuePair(ParameterNameFactory.CODE, authenticationCode));
        nameValues.add(new BasicNameValuePair(ParameterNameFactory.SCOPE, scopes));
        nameValues.add(new BasicNameValuePair(ParameterNameFactory.GRANT_TYPE, ParameterNameFactory.AUTHORIZATION_CODE));
        nameValues.add(new BasicNameValuePair(ParameterNameFactory.REDIRECT_URI, redirectUri));
        if (customOidcConfiguration.isPkceRequired()) {
            String codeVerifier = cacheManager.getCache(CommonUtil.generateCacheKey(state, ParameterNameFactory.CODE_VERIFIER));
            nameValues.add(new BasicNameValuePair(ParameterNameFactory.CODE_VERIFIER, codeVerifier));
        }

        HttpPost httpPost = new HttpPost(tokenEndpoint);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValues, StandardCharsets.UTF_8));
        httpPost.setHeader(ParameterNameFactory.AUTHORIZATION, ParameterNameFactory.BASIC + basicAuthHeader);
        httpPost.setHeader(ParameterNameFactory.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        String tokenEndpointInfo = HttpConnectUtil.doPostConnect(httpPost);
        TokenEndpointResponse tokenEndpointResponse = JSON.parseObject(tokenEndpointInfo, TokenEndpointResponse.class);
        return tokenEndpointResponse;
    }
}
