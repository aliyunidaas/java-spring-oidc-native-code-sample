package com.aliyunidaas.sample.controller;

import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.InitConfiguration;
import com.aliyunidaas.sample.common.factory.ConstantParams;
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
    private InitConfiguration initConfiguration;

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
        Cookie cookie = WebUtils.getCookie(request, ConstantParams.COOKIE_NAME);
        if (cookie == null) {
            model.addAttribute(ERROR, ERROR_MESSAGE);
            return "error";
        }
        String cacheKey = cookie.getValue();
        StateObject stateObject = cacheManager.getCache(cacheKey);
        model.addAttribute(ConstantParams.AUTHORIZATION_CODE, stateObject.getAuthorizationCode());
        model.addAttribute(ConstantParams.ACCESS_TOKEN, stateObject.getAccessToken());
        model.addAttribute(ConstantParams.REFRESH_TOKEN, stateObject.getRefreshToken());
        model.addAttribute(ConstantParams.TITLE, getTitle());
        return "otherInfos";
    }

    private String getTitle() {
        if (initConfiguration.getOidcConfig().isPkceRequired()) {
            return ConstantParams.PKCE_AUTHORIZATION_CODE_FLOW;
        }
        return ConstantParams.AUTHORIZATION_CODE_FLOW;
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
        Cookie cookie = WebUtils.getCookie(request, ConstantParams.COOKIE_NAME);
        if (cookie == null) {
            model.addAttribute(ERROR, ERROR_MESSAGE);
            return "error";
        }
        String cacheKey = cookie.getValue();
        StateObject stateObject = cacheManager.getCache(cacheKey);
        Map<String, Object> idTokenClaimsMap = stateObject.getIdTokenClaimsMap();
        model.addAttribute(ConstantParams.PAYLOAD, idTokenClaimsMap);
        model.addAttribute(ConstantParams.TITLE, getTitle());
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
        Cookie cookie = WebUtils.getCookie(request, ConstantParams.COOKIE_NAME);
        if (cookie == null) {
            model.addAttribute(ERROR, ERROR_MESSAGE);
            return "error";
        }
        String cacheKey = cookie.getValue();
        StateObject stateObject = cacheManager.getCache(cacheKey);
        UserInfoEndpointResponse userInfoEndpointDto = stateObject.getUserInfo();
        model.addAttribute(ConstantParams.SUB, userInfoEndpointDto.getSub());
        model.addAttribute(ConstantParams.NAME, userInfoEndpointDto.getName());
        model.addAttribute(ConstantParams.PREFERRED_USERNAME, userInfoEndpointDto.getPreferredUsername());
        model.addAttribute(ConstantParams.EMAIL, userInfoEndpointDto.getEmail());
        model.addAttribute(ConstantParams.TITLE, getTitle());
        return "userInfo";
    }
}
