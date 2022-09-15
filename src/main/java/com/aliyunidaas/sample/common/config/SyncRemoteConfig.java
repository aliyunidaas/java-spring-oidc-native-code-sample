package com.aliyunidaas.sample.common.config;

import com.aliyun.auth.credentials.provider.AnonymousCredentialProvider;
import com.aliyun.sdk.service.eiam_developerapi20220225.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/3 8:16 PM
 * @author: yunqiu
 **/
@Component
public class SyncRemoteConfig implements InitializingBean {

    private AsyncClient remoteClient;


    @Value("${custom.developer-api-endpoint}")
    private String developerApiEndpoint;

    @Override
    public void afterPropertiesSet()  {
        AnonymousCredentialProvider provider = AnonymousCredentialProvider.create();
        remoteClient = AsyncClient
                .builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(ClientOverrideConfiguration
                        .create()
                        .setEndpointOverride(developerApiEndpoint))
                .build();
    }

    public AsyncClient getRemoteClient() {
        return remoteClient;
    }

    public void setRemoteClient(AsyncClient remoteClient) {
        this.remoteClient = remoteClient;
    }
}
