package com.aliyunidaas.sample.controller;

import com.alibaba.fastjson.JSON;
import com.aliyunidaas.sample.common.EndpointContext;
import com.aliyunidaas.sample.common.cache.CacheManager;
import com.aliyunidaas.sample.common.config.CustomOidcConfiguration;
import com.aliyunidaas.sample.common.factory.ParameterNameFactory;
import com.aliyunidaas.sample.common.util.CommonUtil;
import com.aliyunidaas.sample.domain.StateObject;
import com.aliyunidaas.sample.domain.TokenEndpointResponse;
import com.aliyunidaas.sample.domain.UserInfoEndpointResponse;
import com.aliyunidaas.sample.service.LoginService;
import com.aliyunidaas.sample.service.impl.UserInfoServiceImpl;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
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
    private CustomOidcConfiguration customOidcConfiguration;

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
        CommonUtil.setCookie(response, ParameterNameFactory.COOKIE_NAME, cookieValue);
        writeSessionAttribute(tokenEndpointResponse, authenticationCode, userInfo, cookieValue);
        String callBackUri = cacheManager.getCache(CommonUtil.generateCacheKey(state, ParameterNameFactory.URI));
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
        Map<String, Object> idTokenClaimsMap = getIdTokenClaimsMap(idToken);
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
        cacheManager.setCache(CommonUtil.generateCacheKey(cookieValue, ParameterNameFactory.COOKIE_NAME), cookieValue);
    }

    /**
     * (1)在授权码或者PKCE授权码模式下可以不验证,可采用setSkipSignatureVerification跳过验签
     *    i.如需要验签，若验证签名中的jwksUri由第三方提供提供，则存在ssrf的风险，这个是需要注意的
     * (2)隐式流一定要验签名，因为经过了前端，可以篡改【demo不涉及隐式流】
     *
     * @param idToken
     * @return
     */
    private Map<String, Object> getIdTokenClaimsMap(String idToken) {
        try {
            EndpointContext endpointContext = cacheManager.getCache(customOidcConfiguration.getIssuer());
            String jwksUri = endpointContext.getJwksUri();
            HttpsJwks httpsJwks = new HttpsJwks(new URI(jwksUri).toASCIIString());
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setRequireJwtId()
                    .setRequireIssuedAt()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(60)
                    .setVerificationKeyResolver(new HttpsJwksVerificationKeyResolver(httpsJwks))
                    .setExpectedAudience(customOidcConfiguration.getClientId())
                    .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(idToken);
            return jwtClaims.getClaimsMap();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
