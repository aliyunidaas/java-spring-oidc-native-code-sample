package com.aliyunidaas.sample.common;

import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import com.aliyunidaas.sample.common.factory.CodeChallengeMethodFactory;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.common.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/23 7:24 PM
 * @author: longqiuling
 **/
public class SimpleAuthnInterceptor implements HandlerInterceptor {

    private final static String SHA_256 = "SHA-256";

    private final static String ILLEGAL_ACCESS_EXCEPTION_MESSAGE = "the cookie is invalid, please clear the cookie and try again.";

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException, NoSuchAlgorithmException, IllegalAccessException {
        Cookie cookie = WebUtils.getCookie(request, ParameterNameFactory.COOKIE_NAME);
        if (cookie == null) {
            String cacheKey = UUID.randomUUID().toString();
            String redirectUri = commonUtil.getRedirectUri(request);
            String loginUri = getAuthorizationEndpointLoginUri(request, cacheKey, redirectUri);
            if (customOidcConfiguration.isOpenPkce()) {
                loginUri = getPkceAuthorizationEndpointLoginUri(cacheKey, loginUri);
            }
            response.sendRedirect(loginUri);
        } else {
            String cookieValue = cacheManager.getCache(CommonUtil.generateCacheKey(cookie.getValue(), ParameterNameFactory.COOKIE_NAME));
            if (StringUtils.isBlank(cookieValue) || !cookie.getValue().equals(cookieValue)) {
                throw new IllegalAccessException(ILLEGAL_ACCESS_EXCEPTION_MESSAGE);
            }

        }
        return true;
    }

    /**
     * Redirect the user to the authorization endpoint of the authorization server
     * and include some query parameters in the URL of the authorization endpoint
     *
     * @param cacheKey 用于作为缓存的key
     * @return 登录的重定向地址
     */
    private String getAuthorizationEndpointLoginUri(HttpServletRequest request, String cacheKey, String redirectUri) throws MalformedURLException {
        EndpointContext endpointContext = cacheManager.getCache(customOidcConfiguration.getIssuer());
        String authorizationEndpoint = endpointContext.getAuthorizationEndpoint();
        final String clientId = customOidcConfiguration.getClientId();
        final String scopes = customOidcConfiguration.getScopes().replace(" ", "%20");

        final String queryString = request.getQueryString();
        final String requestUrl = request.getRequestURL().toString();
        final URL url = new URL(requestUrl);
        final String state = url.getPath() + (queryString == null ? "" : "?" + queryString) + ":" + cacheKey;
        // responseType = code , It means that this is an authorization code request
        final String loginUrl = String.format(
                "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s",
                authorizationEndpoint,
                clientId,
                redirectUri,
                scopes,
                state);
        return loginUrl;
    }

    /**
     * Obtain the redirection URL of Authorization Code With PKCE Flow
     *
     * @param state    缓存code verifier 的key
     * @param loginUri 授权码情况下的重定向地址
     * @return pkce情况下登录的的重定向地址
     * @throws NoSuchAlgorithmException
     */
    private String getPkceAuthorizationEndpointLoginUri(String state, String loginUri) throws NoSuchAlgorithmException {
        String codeVerifier = createCodeVerifier();
        String codeChallenge = codeVerifier;
        String codeChallengeMethod = customOidcConfiguration.getCodeChallengeMethod();
        if (codeChallengeMethod.equals(CodeChallengeMethodFactory.SHA_256)) {
            codeChallenge = createHash(codeVerifier);
        }
        cacheManager.setCache(CommonUtil.generateCacheKey(state, ParameterNameFactory.CODE_VERIFIER), codeVerifier);
        loginUri = String.format(
                "%s&code_challenge_method=%s&code_challenge=%s",
                loginUri,
                customOidcConfiguration.getCodeChallengeMethod(),
                codeChallenge);
        return loginUri;
    }

    /**
     * generate the code verifier
     *
     * @return code verifier
     */
    private static String createCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[32];
        secureRandom.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    /**
     * codeVerifier uses SHA256 hash encryption and Base64 encoding
     *
     * @param codeVerifier
     * @return 经过处理后的code verifier
     * @throws NoSuchAlgorithmException
     */
    private static String createHash(String codeVerifier) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SHA_256);
        byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

}

