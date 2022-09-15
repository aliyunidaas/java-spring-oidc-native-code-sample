package com.aliyunidaas.sample.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/5 12:37 PM
 * @author: longqiuling
 **/
public class CommonUtil {

    private final static Integer COOKIE_AGE = 3600;

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_AGE);
        response.addCookie(cookie);
    }

    public static String generateCacheKey(String randomValue, String name) {
        return String.format("%s:%s:", name, randomValue);
    }

    public static String generateCacheKey(String instanceId, String applicationId, String object) {
        return String.format("%s:%s:%s:", instanceId, applicationId, object);
    }
}
