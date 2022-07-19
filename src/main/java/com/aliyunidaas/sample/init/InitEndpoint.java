package com.aliyunidaas.sample.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.common.util.HttpConnectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/8 11:01 AM
 * @author: longqiuling
 **/
@Component
public class InitEndpoint {

    private final static String DISCOVER_ENDPOINT = "/.well-known/openid-configuration";

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private HttpConnectUtil httpConnectUtil;

    @PostConstruct
    public void init() throws IOException {
        EndpointContext endpointContext = cacheManager.getCache(customOidcConfiguration.getIssuer());
        if (endpointContext == null) {
            String discoveryEndpoint = customOidcConfiguration.getIssuer() + DISCOVER_ENDPOINT;
            cacheEndpoint(discoveryEndpoint);
        }
    }

    /**
     * Get all endpoint information by issuer's discoveryEndpoint
     *
     * @param discoveryEndpoint discovery端点
     */
    private void cacheEndpoint(String discoveryEndpoint) throws IOException {
        String endpoint = httpConnectUtil.doGetConnect(discoveryEndpoint);
        JSONObject endpointJson = JSON.parseObject(endpoint, JSONObject.class);
        final String authorizationEndpoint = (String)endpointJson.get(ParameterNameFactory.AUTHORIZATION_ENDPOINT);
        final String tokenEndpoint = (String)endpointJson.get(ParameterNameFactory.TOKEN_ENDPOINT);
        final String userinfoEndpoint = (String)endpointJson.get(ParameterNameFactory.USERINFO_ENDPOINT);
        EndpointContext endpointContext = EndpointContext
                .getBuilder()
                .setAuthorizationEndpoint(authorizationEndpoint)
                .setTokenEndpoint(tokenEndpoint)
                .setUserinfoEndpoint(userinfoEndpoint)
                .build();
        cacheManager.setCache(customOidcConfiguration.getIssuer(), endpointContext);
    }
}
