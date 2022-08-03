package com.aliyunidaas.sample.controller;

import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.domain.StateObject;
import com.aliyunidaas.sample.domain.UserInfoEndpointResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description: 这个Controller是为了demo 前端显示使用，在生产应用中不应该输出这些敏感信息给客户端。
 *
 * @date: 2022/7/8 2:49 PM
 * @author: longqiuling
 **/
@Controller
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    private static final String ERROR = "error";

    private static final String ERROR_MESSAGE = " cookie is nul, please login again.";

    /**
     * 前端演示使用，生产应用应该谨慎保持这些敏感数据
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/otherInfos")
    public String getAccessToken(HttpServletRequest request, Model model) {
        Cookie cookie = WebUtils.getCookie(request, ParameterNameFactory.COOKIE_NAME);
        if (cookie == null) {
            model.addAttribute(ERROR, ERROR_MESSAGE);
            return "error";
        }
        String cacheKey = cookie.getValue();
        StateObject stateObject = cacheManager.getCache(cacheKey);
        model.addAttribute(ParameterNameFactory.AUTHORIZATION_CODE, stateObject.getAuthorizationCode());
        model.addAttribute(ParameterNameFactory.ACCESS_TOKEN, stateObject.getAccessToken());
        model.addAttribute(ParameterNameFactory.REFRESH_TOKEN, stateObject.getRefreshToken());
        model.addAttribute(ParameterNameFactory.TITLE, getTitle());
        return "otherInfos";
    }

    private String getTitle() {
        if (customOidcConfiguration.isPkceRequired()) {
            return ParameterNameFactory.PKCE_AUTHORIZATION_CODE_FLOW;
        }
        return ParameterNameFactory.AUTHORIZATION_CODE_FLOW;
    }

    /**
     * 前端演示使用，生产应用应该谨慎保持这些敏感数据
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/idToken")
    public String getIdToken(HttpServletRequest request, Model model) {
        Cookie cookie = WebUtils.getCookie(request, ParameterNameFactory.COOKIE_NAME);
        if (cookie == null) {
            model.addAttribute(ERROR, ERROR_MESSAGE);
            return "error";
        }
        String cacheKey = cookie.getValue();
        StateObject stateObject = cacheManager.getCache(cacheKey);
        Map<String, Object> idTokenClaimsMap = stateObject.getIdTokenClaimsMap();
        model.addAttribute(ParameterNameFactory.PAYLOAD, idTokenClaimsMap);
        model.addAttribute(ParameterNameFactory.TITLE, getTitle());
        return "idToken";
    }

    /**
     * 前端演示使用，生产应用应该谨慎保持这些敏感数据
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/userInfo")
    public String getUserInfo(HttpServletRequest request, Model model) {
        Cookie cookie = WebUtils.getCookie(request, ParameterNameFactory.COOKIE_NAME);
        if (cookie == null) {
            model.addAttribute(ERROR, ERROR_MESSAGE);
            return "error";
        }
        String cacheKey = cookie.getValue();
        StateObject stateObject = cacheManager.getCache(cacheKey);
        UserInfoEndpointResponse userInfoEndpointDto = stateObject.getUserInfo();
        model.addAttribute(ParameterNameFactory.SUB, userInfoEndpointDto.getSub());
        model.addAttribute(ParameterNameFactory.NAME, userInfoEndpointDto.getName());
        model.addAttribute(ParameterNameFactory.PREFERRED_USERNAME, userInfoEndpointDto.getPreferredUsername());
        model.addAttribute(ParameterNameFactory.EMAIL, userInfoEndpointDto.getEmail());
        model.addAttribute(ParameterNameFactory.TITLE, getTitle());
        return "userInfo";
    }
}
