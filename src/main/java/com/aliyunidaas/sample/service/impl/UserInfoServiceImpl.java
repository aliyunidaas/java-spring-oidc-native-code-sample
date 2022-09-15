package com.aliyunidaas.sample.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.InitConfiguration;
import com.aliyunidaas.sample.common.factory.ConstantParams;
import com.aliyunidaas.sample.common.util.HttpConnectUtil;
import com.aliyunidaas.sample.domain.UserInfoEndpointResponse;
import com.aliyunidaas.sample.service.UserInfoService;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/27 11:26 AM
 * @author: longqiuling
 **/
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private InitConfiguration initConfiguration;

    @Override
    public UserInfoEndpointResponse getUserInfo(String accessToken) throws IOException {
        EndpointContext endpointContext = cacheManager.getCache(initConfiguration.getOidcConfig().getIssuer());
        final String userInfoEndpoint = endpointContext.getUserinfoEndpoint();

        List<NameValuePair> nameValues = new ArrayList<>();
        nameValues.add(new BasicNameValuePair(ConstantParams.ACCESS_TOKEN, accessToken));

        HttpPost httpPost = new HttpPost(userInfoEndpoint);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValues, StandardCharsets.UTF_8));
        httpPost.setHeader(ConstantParams.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        String userInfo = HttpConnectUtil.doPostConnect(httpPost);
        UserInfoEndpointResponse userInfoEndpointDTO = JSON.parseObject(userInfo, UserInfoEndpointResponse.class);
        return userInfoEndpointDTO;
    }
}
