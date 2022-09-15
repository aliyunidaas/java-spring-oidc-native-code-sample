package com.aliyunidaas.sample.init;

import com.alibaba.fastjson.JSON;
import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.InitConfiguration;
import com.aliyunidaas.sample.common.util.HttpConnectUtil;
import com.aliyunidaas.sample.remote.EventDataCallbackHandler;
import com.aliyunidaas.sync.event.runner.EventDataRunner;
import com.aliyunidaas.sync.log.DefaultSimpleLogger;
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
    private InitConfiguration initConfiguration;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EventDataCallbackHandler eventDataCallbackHandler;

    @PostConstruct
    public void init() throws IOException {
        final InitConfiguration.OidcConfig oidcConfig = initConfiguration.getOidcConfig();
        EndpointContext endpointContext = cacheManager.getCache(oidcConfig.getIssuer());
        if (endpointContext == null) {
            String discoveryEndpoint = oidcConfig.getIssuer() + DISCOVER_ENDPOINT;
            cacheEndpoint(discoveryEndpoint);
        }
        final InitConfiguration.SyncConfig syncConfig = initConfiguration.getSyncConfig();
        final EventDataRunner eventDataRunner = new EventDataRunner();
        eventDataRunner.setEncryptKey(syncConfig.getEncryptKey());
        eventDataRunner.setJwkUrl(syncConfig.getJwksUri());
        eventDataRunner.setAppId(initConfiguration.getApplicationId());
        eventDataRunner.setSimpleLogger(new DefaultSimpleLogger());
        eventDataCallbackHandler.putEventDataRunner(initConfiguration.getApplicationId(), eventDataRunner);
    }

    /**
     * Get all endpoint information by issuer's discoveryEndpoint
     *
     * @param discoveryEndpoint discovery端点
     */
    private void cacheEndpoint(String discoveryEndpoint) throws IOException {
        String discoverEndpointInfo = HttpConnectUtil.doGetConnect(discoveryEndpoint);
        EndpointContext endpointContext = JSON.parseObject(discoverEndpointInfo, EndpointContext.class);
        final InitConfiguration.OidcConfig oidcConfig = initConfiguration.getOidcConfig();
        cacheManager.setCache(oidcConfig.getIssuer(), endpointContext);
    }
}
