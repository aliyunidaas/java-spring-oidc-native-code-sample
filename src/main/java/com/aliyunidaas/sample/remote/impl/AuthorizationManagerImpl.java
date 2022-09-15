package com.aliyunidaas.sample.remote.impl;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.GenerateTokenResponseBody;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.util.CommonUtil;
import com.aliyunidaas.sample.domain.SyncAccessToken;
import com.aliyunidaas.sample.remote.AuthorizationManager;
import com.aliyunidaas.sample.remote.TokenRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/8 4:15 PM
 * @author: yunqiu
 **/
@Component
public class AuthorizationManagerImpl implements AuthorizationManager {

    private static final String BEARER = "Bearer ";

    private static final String BEARER_ACCESS_TOKEN = "bearer access token";

    @Autowired
    private TokenRemoteService tokenRemoteService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public String getAuthorization(String instanceId, String applicationId, String clientId, String clientSecret) {
        final SyncAccessToken apiAccessToken = cacheManager.getCache(CommonUtil.generateCacheKey(instanceId, applicationId, BEARER_ACCESS_TOKEN));
        // 有效性判断，有效性预留5分钟的时间窗口，避免使用时可能是无效的情况
        if (null != apiAccessToken && apiAccessToken.getExpiresAt() - System.currentTimeMillis() / 1000 > 300000L) {
            return BEARER + apiAccessToken.getAccessToken();
        }
        final GenerateTokenResponseBody tokenResponseBody = tokenRemoteService.generateToken(instanceId, applicationId, clientId, clientSecret);
        SyncAccessToken syncAccessToken = SyncAccessToken
                .getBuilder()
                .setAccessToken(tokenResponseBody.getAccessToken())
                .setExpiresAt(tokenResponseBody.getExpiresAt())
                .setExpiresIn(tokenResponseBody.getExpiresIn())
                .build();
        cacheManager.setCache(CommonUtil.generateCacheKey(instanceId, applicationId, BEARER_ACCESS_TOKEN), syncAccessToken);
        return BEARER + syncAccessToken.getAccessToken();
    }
}
