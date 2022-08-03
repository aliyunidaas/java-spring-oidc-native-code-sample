package com.aliyunidaas.sample.service;

import com.aliyunidaas.sample.domain.TokenEndpointResponse;

import java.io.IOException;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/27 11:26 AM
 * @author: longqiuling
 **/
public interface LoginService {
    /**
     * 通过授权码去授权端点换取令牌信息
     *
     * @param authenticationCode 授权码
     * @param state              随机值，防攻击使用
     * @return
     * @throws IOException
     */
    TokenEndpointResponse swapCodeFromTokenEndpoint(String authenticationCode, String state) throws IOException;

}
