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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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

    private final static String UTF_8 = "UTF-8";

    @Autowired
    private CustomOidcConfiguration customOidcConfiguration;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException, NoSuchAlgorithmException, IllegalAccessException {
        Cookie cookie = WebUtils.getCookie(request, ParameterNameFactory.COOKIE_NAME);
        if (cookie == null) {
            String state = UUID.randomUUID().toString();
            String redirectUri = customOidcConfiguration.getRedirectUri();
            String iDaaSLoginUri = getIDaaSLoginUri(request, state, redirectUri);
            if (customOidcConfiguration.isPkceRequired()) {
                iDaaSLoginUri = getIDaaSLoginUri(state, iDaaSLoginUri);
            }
            response.sendRedirect(iDaaSLoginUri);
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
     * @param state 用于作为缓存的key以及作为随机值用于跨站保护
     * @return 登录的重定向地址
     */
    private String getIDaaSLoginUri(HttpServletRequest request, String state, String redirectUri)
            throws MalformedURLException, UnsupportedEncodingException {
        EndpointContext endpointContext = cacheManager.getCache(customOidcConfiguration.getIssuer());
        String authorizationEndpoint = endpointContext.getAuthorizationEndpoint();
        final String clientId = customOidcConfiguration.getClientId();
        final String scopes = customOidcConfiguration.getScopes().replace(" ", "%20");

        final String queryString = request.getQueryString();
        final String requestUrl = request.getRequestURL().toString();
        final URL url = new URL(requestUrl);
        String callbackUrl = url.getPath() + (queryString == null ? "" : "?" + queryString);
        cacheManager.setCache(CommonUtil.generateCacheKey(state, ParameterNameFactory.URI), callbackUrl);
        // responseType = code , It means that this is an authorization code request
        return authorizationEndpoint +
                "?response_type=code"
                + "&client_id=" + URLEncoder.encode(clientId, UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, UTF_8)
                + "&scope=" + URLEncoder.encode(scopes, UTF_8)
                + "&state=" + URLEncoder.encode(state, UTF_8);
    }

    /**
     * Obtain the redirection URL of Authorization Code With PKCE Flow
     *
     * @param state    缓存code verifier 的key
     * @param iDaaSLoginUri 授权码情况下的重定向地址
     * @return pkce情况下登录的的重定向地址
     * @throws NoSuchAlgorithmException
     */
    private String getIDaaSLoginUri(String state, String iDaaSLoginUri) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String codeVerifier = createCodeVerifier();
        String codeChallenge = codeVerifier;
        String codeChallengeMethod = customOidcConfiguration.getCodeChallengeMethod();
        if (codeChallengeMethod.equals(CodeChallengeMethodFactory.SHA_256)) {
            codeChallenge = createHash(codeVerifier);
        }
        cacheManager.setCache(CommonUtil.generateCacheKey(state, ParameterNameFactory.CODE_VERIFIER), codeVerifier);
        iDaaSLoginUri = iDaaSLoginUri
                + "&code_challenge_method=" + URLEncoder.encode(customOidcConfiguration.getCodeChallengeMethod(), UTF_8)
                + "&code_challenge=" + URLEncoder.encode(codeChallenge, UTF_8);
        return iDaaSLoginUri;
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
        byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}

