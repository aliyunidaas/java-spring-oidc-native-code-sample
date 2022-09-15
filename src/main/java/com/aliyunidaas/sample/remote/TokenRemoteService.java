package com.aliyunidaas.sample.remote;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.GenerateTokenResponseBody;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/2 6:05 PM
 * @author: yunqiu
 **/
public interface TokenRemoteService {

    /**
     * 生成认证token
     *
     * @param instanceId
     * @param applicationId
     * @return
     */
    GenerateTokenResponseBody generateToken(String instanceId, String applicationId, String clientId, String clientSecret);
}
