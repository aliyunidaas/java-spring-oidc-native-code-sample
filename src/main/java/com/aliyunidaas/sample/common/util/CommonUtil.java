package com.aliyunidaas.sample.common.util;

import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/5 12:37 PM
 * @author: longqiuling
 **/
@Component
public class CommonUtil {

    private final static Integer COOKIE_AGE = 3600;

    private final static String BASE_URI = "${baseUri}";

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    public String getRedirectUri(HttpServletRequest request) {
        String baseRedirectUri = customOidcConfiguration.getRedirectUri();
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String baseUri = requestURL.substring(0, requestURL.length() - requestURI.length());
        return baseRedirectUri.replace(BASE_URI, baseUri);
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_AGE);
        response.addCookie(cookie);
    }

    public static String generateCacheKey(String randomValue, String name) {
        return String.format("%s(%s)", name, randomValue);
    }

}
