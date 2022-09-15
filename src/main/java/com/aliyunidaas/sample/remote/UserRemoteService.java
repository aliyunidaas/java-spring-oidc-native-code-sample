package com.aliyunidaas.sample.remote;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.GetUserResponseBody;
import com.aliyun.sdk.service.eiam_developerapi20220225.models.ListUsersResponseBody;
import com.aliyunidaas.sample.common.params.CreateUserParams;
import com.aliyunidaas.sample.common.params.ListUsersParams;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/1 5:26 PM
 * @author: yunqiu
 **/
public interface UserRemoteService {

    /**
     * 创建用户
     *
     * @param instanceId
     * @param applicationId
     * @return
     * @throws Exception
     */
    String createUser(String accessToken, String instanceId, String applicationId,String userId, CreateUserParams params);

    /**
     * 查询用户列表
     *
     * @param instanceId
     * @param applicationId
     * @return
     */
    ListUsersResponseBody listUsers(String accessToken, String instanceId, String applicationId, ListUsersParams params);

    /**
     * 删除用户
     *
     * @param instanceId
     * @param applicationId
     * @param userId
     */

    void deleteUser(String accessToken, String instanceId, String applicationId, String userId);

    /**
     * 查询一个EIAM用户
     *
     * @param accessToken
     * @param instanceId
     * @param applicationId
     * @param userId
     * @return
     */
    GetUserResponseBody getUser(String accessToken, String instanceId, String applicationId, String userId);

}
