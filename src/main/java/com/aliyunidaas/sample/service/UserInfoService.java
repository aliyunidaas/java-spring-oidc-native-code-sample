package com.aliyunidaas.sample.service;

import com.aliyunidaas.sample.domain.UserInfoEndpointResponse;

import java.io.IOException;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/27 11:26 AM
 * @author: longqiuling
 **/
public interface UserInfoService {
    /**
     * 拿令牌信息去用户端点换取用户信息
     * @param accessToken
     * @return
     * @throws IOException
     */
    UserInfoEndpointResponse getUserInfo(String accessToken) throws IOException;
}
