package com.aliyunidaas.sample.remote;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/8 4:15 PM
 * @author: yunqiu
 **/
public interface AuthorizationManager {
    /**
     * 获取认证信息，所有Developer API接口，都依赖令牌调用。
     *
     * @param instanceId    实例ID
     * @param applicationId 应用ID
     * @param clientId      应用的客户端ID。
     * @param clientSecret  应用的客户端密钥secret。
     * @return 认证信息。 格式:Bearer ${access_token}。 示例：Bearer ATxxxx。
     */
    String getAuthorization(String instanceId, String applicationId, String clientId, String clientSecret);
}
