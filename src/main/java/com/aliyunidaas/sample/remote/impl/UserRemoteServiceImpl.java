package com.aliyunidaas.sample.remote.impl;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.*;
import com.aliyunidaas.sample.common.config.SyncRemoteConfig;
import com.aliyunidaas.sample.common.params.CreateUserParams;
import com.aliyunidaas.sample.common.params.ListUsersParams;
import com.aliyunidaas.sample.exception.RemoteException;
import com.aliyunidaas.sample.remote.UserRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/28 4:14 PM
 * @author: yunqiu
 **/
@Service
public class UserRemoteServiceImpl implements UserRemoteService {

    @Autowired
    private SyncRemoteConfig remoteConfig;

    private static final String ROOT = "ou_root";

    @Override
    public String createUser(String accessToken, String instanceId, String applicationId, String userId,
                             CreateUserParams params) {
        CreateUserRequest createUserRequest = CreateUserRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .username(params.getUserName())
                .phoneNumber(params.getPhoneNumber())
                .email(params.getUserEmail())
                .userExternalId(userId)
                .primaryOrganizationalUnitId(ROOT)
                .phoneRegion("86")
                .phoneNumberVerified(false)
                .emailVerified(false)
                .build();
        try {
            CompletableFuture<CreateUserResponse> response = remoteConfig.getRemoteClient().createUser(createUserRequest);
            CreateUserResponse resp = response.get();
            return resp.getBody().getUserId();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public ListUsersResponseBody listUsers(String accessToken, String instanceId, String applicationId, ListUsersParams params) {
        ListUsersRequest listUsersRequest = ListUsersRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .organizationalUnitId(params.getOrganizationalUnitId())
                .pageNumber(params.getPageNumber())
                .pageSize(params.getPageSize())
                .build();
        try {
            CompletableFuture<ListUsersResponse> response = remoteConfig.getRemoteClient().listUsers(listUsersRequest);
            final ListUsersResponse resp = response.get();
            return resp.getBody();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String accessToken, String instanceId, String applicationId, String userId) {
        DeleteUserRequest request = DeleteUserRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .userId(userId)
                .build();
        try {
            remoteConfig.getRemoteClient().deleteUser(request);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public GetUserResponseBody getUser(String accessToken, String instanceId, String applicationId, String userId) {
        GetUserRequest request = GetUserRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .userId(userId)
                .build();
        try {
            final CompletableFuture<GetUserResponse> response = remoteConfig.getRemoteClient().getUser(request);
            return response.get().getBody();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }
}
