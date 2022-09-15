package com.aliyunidaas.sample.remote.impl;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.GenerateTokenRequest;
import com.aliyun.sdk.service.eiam_developerapi20220225.models.GenerateTokenResponse;
import com.aliyun.sdk.service.eiam_developerapi20220225.models.GenerateTokenResponseBody;
import com.aliyunidaas.sample.common.config.SyncRemoteConfig;
import com.aliyunidaas.sample.exception.RemoteException;
import com.aliyunidaas.sample.remote.TokenRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/2 6:05 PM
 * @author: yunqiu
 **/
@Service
public class TokenRemoteServiceImpl implements TokenRemoteService {

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    @Autowired
    private SyncRemoteConfig remoteConfig;

    @Override
    public GenerateTokenResponseBody generateToken(String instanceId, String applicationId, String clientId, String clientSecret) {
        GenerateTokenRequest request = GenerateTokenRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(CLIENT_CREDENTIALS)
                .build();
        try {
            final CompletableFuture<GenerateTokenResponse> response = remoteConfig.getRemoteClient().generateToken(request);
            return response.get().getBody();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }
}
