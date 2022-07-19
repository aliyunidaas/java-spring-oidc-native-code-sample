package com.aliyunidaas.sample.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.common.util.CommonUtil;
import com.aliyunidaas.sample.service.LoginService;
import com.aliyunidaas.sample.service.UserInfoService;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    private UserInfoService userInfoService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * Process authorization responses.  A successful login will redirect to the user's redirect-uri address.
     * Take the authorization code and send it to the token endpoint using the HTTP POST method.
     *
     * @param authenticationCode authorization code
     * @param state              用于作为缓存的key值，在授权码流中也可用于防止攻击
     * @param model              the model to be passed to the view.
     * @return which view to display.
     */
    @RequestMapping(value = "/authentication/login", method = {RequestMethod.POST, RequestMethod.GET})
    public String login(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam("code") String authenticationCode,
                        @RequestParam("state") String state,
                        Model model) throws IOException {
        String redirectUri = state.split(":")[0];
        String cacheKey = state.split(":")[1];

        LOGGER.info("=== Authentication Code === : {}", authenticationCode);
        String token = loginService.getToken(request, authenticationCode, cacheKey);
        JSONObject rawTokenObject = JSON.parseObject(token, JSONObject.class);
        LOGGER.info("==== Raw Token information ==== :{}", token);

        final String accessToken = (String)rawTokenObject.get(ParameterNameFactory.ACCESS_TOKEN);
        String userInfo = userInfoService.getUserInfo(accessToken);
        LOGGER.info("==== User Info information ==== :{}", userInfo);

        String cookieValue = UUID.randomUUID().toString();
        CommonUtil.setCookie(response, ParameterNameFactory.COOKIE_NAME, cookieValue);
        writeSessionAttribute(rawTokenObject, authenticationCode, userInfo, cookieValue);

        return "redirect:" + redirectUri;

    }

    /**
     * the information to display on the page and store
     *
     * @param rawTokenObject     token端点返回的信息
     * @param authenticationCode authorization code
     * @param userInfo           information returned by the user endpoint
     */
    private void writeSessionAttribute(JSONObject rawTokenObject, String authenticationCode, String userInfo, String cookieValue) {
        final String accessToken = (String)rawTokenObject.get(ParameterNameFactory.ACCESS_TOKEN);
        final String idToken = (String)rawTokenObject.get(ParameterNameFactory.ID_TOKEN);
        Map<String, Object> idTokenClaimsMap = getIdTokenClaimsMap(idToken);
        final String refreshToken = (String)rawTokenObject.get(ParameterNameFactory.REFRESH_TOKEN);
        JSONObject userMessage = JSON.parseObject(userInfo, JSONObject.class);

        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ParameterNameFactory.AUTHORIZATION_CODE), authenticationCode);
        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ParameterNameFactory.ACCESS_TOKEN), accessToken);
        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ParameterNameFactory.ID_TOKEN), idTokenClaimsMap);
        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ParameterNameFactory.REFRESH_TOKEN), refreshToken);
        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ParameterNameFactory.USER_INFO), userMessage);
        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ParameterNameFactory.COOKIE_NAME), cookieValue);
    }

    private Map<String, Object> getIdTokenClaimsMap(String idToken) {
        try {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setRequireJwtId()
                    .setRequireIssuedAt()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(60)
                    .setSkipSignatureVerification()
                    .setSkipDefaultAudienceValidation()
                    .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(idToken);
            return jwtClaims.getClaimsMap();
        } catch (InvalidJwtException e) {
            throw new RuntimeException(e);
        }
    }

}
