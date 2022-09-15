package com.aliyunidaas.sample.remote.impl;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.*;
import com.aliyunidaas.sample.common.config.SyncRemoteConfig;
import com.aliyunidaas.sample.common.params.CreateOuParams;
import com.aliyunidaas.sample.common.params.ListOuParams;
import com.aliyunidaas.sample.exception.RemoteException;
import com.aliyunidaas.sample.remote.OuRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/2 5:25 PM
 * @author: yunqiu
 **/
@Service
public class OuRemoteServiceImpl implements OuRemoteService {

    @Autowired
    private SyncRemoteConfig remoteConfig;

    @Override
    public String createOrganizationalUnit(String accessToken, String instanceId, String applicationId, CreateOuParams params) {
        CreateOrganizationalUnitRequest request = CreateOrganizationalUnitRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .organizationalUnitName(params.getOuName())
                .build();
        try {
            final CompletableFuture<CreateOrganizationalUnitResponse> organizationalUnit =
                    remoteConfig.getRemoteClient().createOrganizationalUnit(request);
            return organizationalUnit.get().getBody().getOrganizationalUnitId();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public void patchOrganizationalUnit(String accessToken, String instanceId, String applicationId, String organizationalUnitId,
                                        String ouId, String ouName) {
        PatchOrganizationalUnitRequest request = PatchOrganizationalUnitRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .organizationalUnitId(organizationalUnitId)
                .organizationalUnitName(ouName)
                .build();
        try {
            remoteConfig.getRemoteClient().patchOrganizationalUnit(request);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public GetOrganizationalUnitResponseBody getOrganizationalUnit(String accessToken, String instanceId, String applicationId,
                                                                   String organizationalUnitId) {
        GetOrganizationalUnitRequest request = GetOrganizationalUnitRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .organizationalUnitId(organizationalUnitId)
                .build();
        try {
            final CompletableFuture<GetOrganizationalUnitResponse> organizationalUnit = remoteConfig.getRemoteClient().getOrganizationalUnit(request);
            return organizationalUnit.get().getBody();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteOrganizationalUnit(String accessToken, String instanceId, String applicationId, String organizationalUnitId) {
        DeleteOrganizationalUnitRequest request = DeleteOrganizationalUnitRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .organizationalUnitId(organizationalUnitId)
                .build();
        try {
            remoteConfig.getRemoteClient().deleteOrganizationalUnit(request);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public ListOrganizationalUnitParentIdsResponseBody listOrganizationalUnitParentIds(String accessToken, String instanceId, String applicationId,
                                                                                       String organizationalUnitId) {
        ListOrganizationalUnitParentIdsRequest request = ListOrganizationalUnitParentIdsRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .organizationalUnitId(organizationalUnitId)
                .build();
        try {
            final CompletableFuture<ListOrganizationalUnitParentIdsResponse> response
                    = remoteConfig.getRemoteClient().listOrganizationalUnitParentIds(request);
            return response.get().getBody();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public ListOrganizationalUnitsResponseBody listOrganizationalUnits(String accessToken, String instanceId, String applicationId,
                                                                       ListOuParams params) {
        ListOrganizationalUnitsRequest request = ListOrganizationalUnitsRequest
                .builder()
                .instanceId(instanceId)
                .applicationId(applicationId)
                .authorization(accessToken)
                .parentId(params.getParentId())
                .pageNumber(params.getPageNumber())
                .pageSize(params.getPageSize())
                .build();
        try {
            final CompletableFuture<ListOrganizationalUnitsResponse> response =
                    remoteConfig.getRemoteClient().listOrganizationalUnits(request);
            return response.get().getBody();
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }
}
