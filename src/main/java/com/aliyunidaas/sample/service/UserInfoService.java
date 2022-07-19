package com.aliyunidaas.sample.service;

import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.common.util.CommonUtil;
import com.aliyunidaas.sample.common.util.HttpConnectUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/27 11:26 AM
 * @author: longqiuling
 **/
@Service
public class UserInfoService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    @Autowired
    private HttpConnectUtil httpConnectUtil;

    public String getUserInfo(String accessToken) throws IOException {
        EndpointContext endpointContext = cacheManager.getCache(customOidcConfiguration.getIssuer());
        final String userInfoEndpoint = endpointContext.getUserinfoEndpoint();

        List<NameValuePair> nameValues = new ArrayList<>();
        nameValues.add(new BasicNameValuePair(ParameterNameFactory.ACCESS_TOKEN, accessToken));

        HttpPost httpPost = new HttpPost(userInfoEndpoint);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValues, StandardCharsets.UTF_8));
        httpPost.setHeader(ParameterNameFactory.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        String userInfo = httpConnectUtil.doPostConnect(httpPost);
        return userInfo;
    }

}
