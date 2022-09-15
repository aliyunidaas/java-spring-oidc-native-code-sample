package com.aliyunidaas.sample.controller;

import com.alibaba.fastjson.JSON;
import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.InitConfiguration;
import com.aliyunidaas.sample.common.factory.ConstantParams;
import com.aliyunidaas.sample.common.util.CommonUtil;
import com.aliyunidaas.sample.common.util.JwtUtil;
import com.aliyunidaas.sample.domain.StateObject;
import com.aliyunidaas.sample.domain.TokenEndpointResponse;
import com.aliyunidaas.sample.domain.UserInfoEndpointResponse;
import com.aliyunidaas.sample.service.LoginService;
import com.aliyunidaas.sample.service.impl.UserInfoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/24 11:04 AM
 * @author: longqiuling
 **/
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private InitConfiguration initConfiguration;

    /**
     * Process authorization responses.  A successful login will redirect to the user's redirect-uri address.
     * Take the authorization code and send it to the token endpoint using the HTTP POST method.
     *
     * @param response
     * @param authenticationCode
     * @param state
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/authentication/login", method = {RequestMethod.POST, RequestMethod.GET})
    public String login(HttpServletResponse response,
                        @RequestParam("code") String authenticationCode,
                        @RequestParam("state") String state) throws IOException {

        LOGGER.info("=== Authentication Code === : {}", authenticationCode);
        TokenEndpointResponse tokenEndpointResponse = loginService.swapCodeFromTokenEndpoint(authenticationCode, state);
        LOGGER.info("==== Raw Token information ==== :{}", JSON.toJSONString(tokenEndpointResponse));

        final String accessToken = tokenEndpointResponse.getAccessToken();
        UserInfoEndpointResponse userInfo = userInfoService.getUserInfo(accessToken);
        LOGGER.info("==== User Info information ==== :{}", JSON.toJSONString(userInfo));

        String cookieValue = UUID.randomUUID().toString();
        CommonUtil.setCookie(response, ConstantParams.COOKIE_NAME, cookieValue);
        writeSessionAttribute(tokenEndpointResponse, authenticationCode, userInfo, cookieValue);
        String callBackUri = cacheManager.getCache(CommonUtil.generateCacheKey(state, ConstantParams.URI));
        return "redirect:" + callBackUri;
    }

    /**
     * the information to display on the page and store
     *
     * @param tokenEndpointResponse token端点返回的信息
     * @param authenticationCode    authorization code
     * @param userInfoEndpointDTO   用户信息端点返回的数据
     */
    private void writeSessionAttribute(TokenEndpointResponse tokenEndpointResponse, String authenticationCode,
                                       UserInfoEndpointResponse userInfoEndpointDTO,
                                       String cookieValue) {
        final String accessToken = tokenEndpointResponse.getAccessToken();
        final String idToken = tokenEndpointResponse.getIdToken();
        EndpointContext endpointContext = cacheManager.getCache(initConfiguration.getOidcConfig().getIssuer());
        Map<String, Object> idTokenClaimsMap = JwtUtil.getIdTokenClaimsMap(idToken, endpointContext,initConfiguration.getOidcConfig().getClientId());
        final String refreshToken = tokenEndpointResponse.getRefreshToken();

        StateObject stateObject = StateObject
                .getBuilder()
                .setAuthorizationCode(authenticationCode)
                .setAccessToken(accessToken)
                .setIdTokenClaimsMap(idTokenClaimsMap)
                .setRefreshToken(refreshToken)
                .setUserInfo(userInfoEndpointDTO)
                .build();
        cacheManager.setCache(cookieValue, stateObject);
        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ConstantParams.COOKIE_NAME), cookieValue);
    }
}
