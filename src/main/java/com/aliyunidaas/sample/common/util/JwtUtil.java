package com.aliyunidaas.sample.common.util;

import com.aliyunidaas.sample.common.EndpointContext;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

import java.net.URI;
import java.util.Map;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/27 8:15 PM
 * @author: yunqiu
 **/
public class JwtUtil {

    /**
     * (1)在授权码或者PKCE授权码模式下可以不验证,可采用setSkipSignatureVerification跳过验签
     * i.如需要验签，若验证签名中的jwksUri由第三方提供提供，则存在ssrf的风险，这个是需要注意的
     * (2)隐式流一定要验签名，因为经过了前端，可以篡改【demo不涉及隐式流】
     *
     * @param idToken
     * @return
     */
    public static Map<String, Object> getIdTokenClaimsMap(String idToken, EndpointContext endpointContext, String clientId) {
        try {
            String jwksUri = endpointContext.getJwksUri();
            HttpsJwks httpsJwks = new HttpsJwks(new URI(jwksUri).toASCIIString());
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setRequireJwtId()
                    .setRequireIssuedAt()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(60)
                    .setVerificationKeyResolver(new HttpsJwksVerificationKeyResolver(httpsJwks))
                    .setExpectedAudience(clientId)
                    .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(idToken);
            return jwtClaims.getClaimsMap();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
