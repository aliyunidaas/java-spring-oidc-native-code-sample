package com.aliyunidaas.sample.init;

import com.alibaba.fastjson.JSON;
import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
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

    /**
     * @see " https://openid.net/specs/openid-connect-discovery-1_0.html"
     */
    private final static String DISCOVER_ENDPOINT = "/.well-known/openid-configuration";

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    @Autowired
    private CacheManager cacheManager;

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
        String discoverEndpointInfo = HttpConnectUtil.doGetConnect(discoveryEndpoint);
        EndpointContext endpointContext = JSON.parseObject(discoverEndpointInfo, EndpointContext.class);
        cacheManager.setCache(customOidcConfiguration.getIssuer(), endpointContext);
    }
}
