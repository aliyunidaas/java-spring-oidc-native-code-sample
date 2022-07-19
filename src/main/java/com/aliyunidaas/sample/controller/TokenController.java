package com.aliyunidaas.sample.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.common.util.CommonUtil;
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
 * Description:
 *
 * @date: 2022/7/8 2:49 PM
 * @author: longqiuling
 **/
@Controller
@RequestMapping("/token")
public class TokenController {

    private static final String ERROR = "error";

    private static final String ERROR_MESSAGE = "please clear the cookie and try again.";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    /**
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
        String authorizationCode = cacheManager.getCache(CommonUtil.generateCacheKey(cacheKey, ParameterNameFactory.AUTHORIZATION_CODE));
        String accessToken = cacheManager.getCache(CommonUtil.generateCacheKey(cacheKey, ParameterNameFactory.ACCESS_TOKEN));
        String refreshToken = cacheManager.getCache(CommonUtil.generateCacheKey(cacheKey, ParameterNameFactory.REFRESH_TOKEN));

        model.addAttribute(ParameterNameFactory.AUTHORIZATION_CODE, authorizationCode);
        model.addAttribute(ParameterNameFactory.ACCESS_TOKEN, accessToken);
        model.addAttribute(ParameterNameFactory.REFRESH_TOKEN, refreshToken);
        model.addAttribute(ParameterNameFactory.TITLE, getTitle());
        return "otherInfos";
    }

    private String getTitle() {
        if (customOidcConfiguration.isOpenPkce()) {
            return ParameterNameFactory.PKCE_AUTHORIZATION_CODE_FLOW;
        }
        return ParameterNameFactory.AUTHORIZATION_CODE_FLOW;
    }

    /**
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
        Map<String, Object> idTokenClaimsMap = cacheManager.getCache(CommonUtil.generateCacheKey(cacheKey, ParameterNameFactory.ID_TOKEN));
        model.addAttribute(ParameterNameFactory.PAYLOAD, idTokenClaimsMap);
        model.addAttribute(ParameterNameFactory.TITLE, getTitle());
        return "idToken";
    }

    /**
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
        JSONObject userMessage = cacheManager.getCache(CommonUtil.generateCacheKey(cacheKey, ParameterNameFactory.USER_INFO));
        model.addAttribute(ParameterNameFactory.USER_INFO, userMessage);
        model.addAttribute(ParameterNameFactory.TITLE, getTitle());
        return "userInfo";
    }
}
