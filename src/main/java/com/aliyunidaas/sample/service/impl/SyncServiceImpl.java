package com.aliyunidaas.sample.service.impl;

import com.aliyun.sdk.service.eiam_developerapi20220225.models.GetOrganizationalUnitResponseBody;
import com.aliyun.sdk.service.eiam_developerapi20220225.models.ListOrganizationalUnitsResponseBody;
import com.aliyun.sdk.service.eiam_developerapi20220225.models.ListUsersResponseBody;
import com.aliyunidaas.sample.common.config.InitConfiguration;
import com.aliyunidaas.sample.common.factory.ConstantParams;
import com.aliyunidaas.sample.common.params.ListOuParams;
import com.aliyunidaas.sample.common.params.ListUsersParams;
import com.aliyunidaas.sample.dao.OuRepository;
import com.aliyunidaas.sample.dao.UserRepository;
import com.aliyunidaas.sample.entity.OuDO;
import com.aliyunidaas.sample.entity.UserDO;
import com.aliyunidaas.sample.remote.AuthorizationManager;
import com.aliyunidaas.sample.remote.EventDataCallbackHandler;
import com.aliyunidaas.sample.remote.OuRemoteService;
import com.aliyunidaas.sample.remote.UserRemoteService;
import com.aliyunidaas.sample.service.SyncService;
import com.aliyunidaas.sync.event.objects.ErrorResponseObject;
import com.aliyunidaas.sync.event.objects.RequestObject;
import com.aliyunidaas.sync.event.objects.ResponseObject;
import com.aliyunidaas.sync.event.objects.SuccessResponseObject;
import com.aliyunidaas.sync.event.runner.EventDataRunner;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/29 4:30 PM
 * @author: yunqiu
 **/
@Service
public class SyncServiceImpl implements SyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncService.class);

    private static final int PAGE_SIZE = 20;

    private static final String ROOT = "ou_root";

    private static final Long MAX_CYCLES_NUM = 10000L;

    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private OuRemoteService ouRemoteService;

    @Autowired
    private InitConfiguration initConfiguration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OuRepository ouRepository;

    @Autowired
    private AuthorizationManager authorizationManager;

    @Autowired
    private EventDataCallbackHandler eventDataCallbackHandler;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ResponseObject receiveSync(RequestObject callbackRequestObject) {
        if (callbackRequestObject == null || StringUtils.isBlank(callbackRequestObject.getEvent())) {
            return new SuccessResponseObject();
        }
        try {
            // 应用未开启接受IDaaS账户同步数据处理开关
            InitConfiguration.SyncConfig syncConfig = initConfiguration.getSyncConfig();
            if (!syncConfig.isEnabled()) {
                return new ErrorResponseObject();
            }
            EventDataRunner eventDataRunner = eventDataCallbackHandler.getEventDataRunner(initConfiguration.getApplicationId());
            eventDataRunner.setEventDataCallback(eventDataCallbackHandler.getDefaultEventDataCallback());
            return eventDataRunner.dispatchEventData(callbackRequestObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void triggerSync(String instanceId, String applicationId) {
        InitConfiguration.OidcConfig oidcConfig = initConfiguration.getOidcConfig();
        String clientId = oidcConfig.getClientId();
        String clientSecret = oidcConfig.getClientSecret();
        Deque<String> queue = new ArrayDeque<>();
        queue.push(ROOT);
        // 广度优先遍历，依次将组织和用户写入数据库，避免再同步过程中移动组织、用户。以免目录结构出错，
        // Demo中将external_id和ou_external_id，设置唯一索引，若发现同步过程中组织重复，则抛错回滚。
        do {
            String curParentId = queue.pop();
            // 同步一个组织下的组织和用户
            syncSingleOrganizationalUnit(instanceId, applicationId, clientId, clientSecret, curParentId);
            // 将该组织下的子组织入栈
            pushSubOrganizationalUnit(instanceId, applicationId, clientId, clientSecret, curParentId, queue);
        } while (!queue.isEmpty());
    }

    /**
     * 同步一个组织下的组织和用户
     *
     * @param instanceId    实例ID
     * @param applicationId 应用ID
     * @param clientId      客户端密钥ID
     * @param clientSecret  客户端密钥secret
     * @param ouExternalId  IDaaS 组织ID
     */
    private void syncSingleOrganizationalUnit(String instanceId, String applicationId, String clientId, String clientSecret, String ouExternalId) {
        String accessToken = authorizationManager.getAuthorization(instanceId, applicationId, clientId, clientSecret);
        GetOrganizationalUnitResponseBody ouResponse = ouRemoteService.getOrganizationalUnit(accessToken, instanceId, applicationId, ouExternalId);
        String ouId = UUID.randomUUID().toString();
        OuDO ouDO = OuDO.getBuilder()
                        .setOuId(ouId)
                        .setOuExternalId(ouResponse.getOrganizationalUnitExternalId())
                        .setOuName(ouResponse.getOrganizationalUnitName())
                        .setParentOuId(ouResponse.getParentId())
                        .build();
        ouRepository.save(ouDO);
        syncSingleOrganizationalUnitUsers(instanceId, applicationId, clientId, clientSecret, ouId, ouExternalId);
    }

    /**
     * 同步一个组织下的用户
     *
     * @param instanceId    实例ID
     * @param applicationId 应用ID
     * @param clientId      客户端密钥ID
     * @param clientSecret  客户端密钥secret
     * @param ouId          Demo的组织ID
     * @param ouExternalId  IDaaS的组织ID
     */
    private void syncSingleOrganizationalUnitUsers(String instanceId, String applicationId, String clientId, String clientSecret, String ouId,
                                                   String ouExternalId) {
        int pageIndex = 0;
        ListUsersResponseBody listUsersResponseBody;
        do {
            String accessToken = authorizationManager.getAuthorization(instanceId, applicationId, clientId, clientSecret);
            ListUsersParams listUsersRequest = new ListUsersParams(ouExternalId, ++pageIndex, PAGE_SIZE);
            listUsersResponseBody = userRemoteService.listUsers(accessToken, instanceId, applicationId, listUsersRequest);
            List<ListUsersResponseBody.Data> userDataList = listUsersResponseBody.getData();
            // 用户信息
            List<UserDO> userDOList = new ArrayList<>();
            for (ListUsersResponseBody.Data userData : userDataList) {
                String userId = UUID.randomUUID().toString();
                userDOList.add(UserDO.getBuilder().setUserId(userId)
                                     .setUsername(userData.getUsername())
                                     .setPhoneRegion(userData.getPhoneRegion())
                                     .setPhoneNumber(userData.getPhoneNumber())
                                     .setUserEmail(userData.getEmail())
                                     .setExternalId(userData.getUserId())
                                     .setOuId(ouId)
                                     .setExternalOuId(ouExternalId)
                                     .build());
            }
            userRepository.saveAll(userDOList);
            validCycleNumber(pageIndex);
        } while ((long)PAGE_SIZE * pageIndex < listUsersResponseBody.getTotalCount());
    }

    /**
     * 校验循环次数是否超过{@code  MAX_CYCLES_NUM}
     *
     * @param pageIndex 总数量
     */
    private void validCycleNumber(int pageIndex) {
        if (pageIndex < MAX_CYCLES_NUM) {
            LOGGER.error(ConstantParams.EXCEED_MAX_CYCLES_NUM);
            throw new RuntimeException(ConstantParams.EXCEED_MAX_CYCLES_NUM);
        }
    }

    /**
     * 将IDaaS中某个组织下的子组织入栈
     *
     * @param instanceId    实例ID
     * @param applicationId 应用ID
     * @param clientId      客户端密钥ID
     * @param clientSecret  客户端密钥secret
     * @param ouExternalId  IDaaS的组织ID
     * @param queue         栈
     */
    private void pushSubOrganizationalUnit(String instanceId, String applicationId, String clientId, String clientSecret, String ouExternalId,
                                           Deque<String> queue) {
        int pageIndex = 0;
        ListOrganizationalUnitsResponseBody ouResponseBody;
        do {
            String accessToken = authorizationManager.getAuthorization(instanceId, applicationId, clientId, clientSecret);
            ListOuParams request = new ListOuParams(ouExternalId, ++pageIndex, PAGE_SIZE);
            ouResponseBody = ouRemoteService.listOrganizationalUnits(accessToken, instanceId, applicationId, request);
            if (ouResponseBody.getData().size() != 0) {
                List<String> ouList = ouResponseBody.getData()
                                                    .stream()
                                                    .map(ListOrganizationalUnitsResponseBody.Data::getOrganizationalUnitId)
                                                    .collect(Collectors.toList());
                ouList.forEach(queue::push);
            }
            validCycleNumber(pageIndex);
        } while ((long)PAGE_SIZE * pageIndex < ouResponseBody.getTotalCount());
    }
}
