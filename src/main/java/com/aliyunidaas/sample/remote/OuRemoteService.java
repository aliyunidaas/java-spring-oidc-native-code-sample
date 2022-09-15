package com.aliyunidaas.sample.remote;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.GetOrganizationalUnitResponseBody;
import com.aliyun.sdk.service.eiam_developerapi20220225.models.ListOrganizationalUnitParentIdsResponseBody;
import com.aliyun.sdk.service.eiam_developerapi20220225.models.ListOrganizationalUnitsResponseBody;
import com.aliyunidaas.sample.common.params.CreateOuParams;
import com.aliyunidaas.sample.common.params.ListOuParams;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/1 5:26 PM
 * @author: yunqiu
 **/
public interface OuRemoteService {

    String createOrganizationalUnit(String accessToken, String instanceId, String applicationId, CreateOuParams params);

    void patchOrganizationalUnit(String accessToken, String instanceId, String applicationId, String organizationalUnitId, String ouId,
                                 String ouName);

    GetOrganizationalUnitResponseBody getOrganizationalUnit(String accessToken, String instanceId, String applicationId, String ouId);

    void deleteOrganizationalUnit(String accessToken, String instanceId, String applicationId, String organizationalUnitId);

    ListOrganizationalUnitParentIdsResponseBody listOrganizationalUnitParentIds(String accessToken, String instanceId, String applicationId,
                                                                                String ouId);

    ListOrganizationalUnitsResponseBody listOrganizationalUnits(String accessToken, String instanceId, String applicationId,
                                                                ListOuParams params);

}
