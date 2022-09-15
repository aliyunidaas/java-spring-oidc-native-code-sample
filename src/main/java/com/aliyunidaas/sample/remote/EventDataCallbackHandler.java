package com.aliyunidaas.sample.remote;

import com.aliyunidaas.sample.service.SyncCallbackService;
import com.aliyunidaas.sync.event.bizdata.OrganizationalUnitInfo;
import com.aliyunidaas.sync.event.bizdata.UserInfo;
import com.aliyunidaas.sync.event.callback.OrganizationalUnitCallback;
import com.aliyunidaas.sync.event.callback.OrganizationalUnitPushCallback;
import com.aliyunidaas.sync.event.callback.UserCallback;
import com.aliyunidaas.sync.event.callback.UserPushCallback;
import com.aliyunidaas.sync.event.callback.impl.DefaultEventDataCallbackImpl;
import com.aliyunidaas.sync.event.callback.objects.EventDataResponse;
import com.aliyunidaas.sync.event.context.EventContext;
import com.aliyunidaas.sync.event.runner.EventDataRunner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/15 7:06 PM
 * @author: yunqiu
 **/
@Component
public class EventDataCallbackHandler implements InitializingBean {

    private final DefaultEventDataCallbackImpl defaultEventDataCallback = new DefaultEventDataCallbackImpl();

    private final Map<String, EventDataRunner> eventDataRunnerMap = new ConcurrentHashMap<>();

    @Autowired
    private SyncCallbackService syncCallbackService;

    @Override
    public void afterPropertiesSet() {
        // 用户相关
        defaultEventDataCallback.registerUserCallback(new UserCallback() {
            @Override
            public EventDataResponse onUserCreate(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userCreateEvent(userInfo);
                // 返回一个成功结果（收到请求后，需要在 10 秒内以 HTTP 200 状态码 响应该请求，否则 IDaaS 会视此次推送失败并以1s、5s、10s、10s、10s 的间隔重新推送事件，最多重试 5 次。）
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserDelete(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userDeleteEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserUpdateInfo(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userUpdateInfoEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserUpdatePassword(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userUpdatePasswordEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserUpdateDisabled(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userDisableEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserUpdateEnabled(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userEnableEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserLock(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userLockEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserUnlock(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userUnlockEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onUserUpdatePrimaryOu(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userUpdatePrimaryOuEvent(userInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }
        });

        // 全量推送范围（用户）
        defaultEventDataCallback.registerUserPushCallback(new UserPushCallback() {
            @Override
            public EventDataResponse onUserPush(EventContext eventContext, UserInfo userInfo) {
                syncCallbackService.userPushEvent(userInfo);
                // 返回一个成功结果（收到请求后，需要在 10 秒内以 HTTP 200 状态码 响应该请求，否则 IDaaS 会视此次推送失败并以1s、5s、10s、10s、10s 的间隔重新推送事件，最多重试 5 次。）
                return EventDataResponse.newSuccessEventDataResponse();
            }
        });
        // 全量推送范围（组织）
        defaultEventDataCallback.registerOrganizationalUnitPushCallback(new OrganizationalUnitPushCallback() {
            @Override
            public EventDataResponse onOuPush(EventContext eventContext, OrganizationalUnitInfo ouInfo) {
                syncCallbackService.ouPushEvent(ouInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }
        });
        // 组织相关
        defaultEventDataCallback.registerOrganizationalUnitCallback(new OrganizationalUnitCallback() {
            @Override
            public EventDataResponse onOuCreate(EventContext eventContext, OrganizationalUnitInfo ouInfo) {
                syncCallbackService.ouCreateEvent(ouInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onOuDelete(EventContext eventContext, OrganizationalUnitInfo ouInfo) {
                syncCallbackService.ouDeleteEvent(ouInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onOuUpdate(EventContext eventContext, OrganizationalUnitInfo ouInfo) {
                syncCallbackService.ouUpdateEvent(ouInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

            @Override
            public EventDataResponse onOuUpdateOrganizationalUnit(EventContext eventContext, OrganizationalUnitInfo ouInfo) {
                syncCallbackService.ouUpdateParentOrganizationalUnitEvent(ouInfo);
                return EventDataResponse.newSuccessEventDataResponse();
            }

        });
    }

    public DefaultEventDataCallbackImpl getDefaultEventDataCallback() {
        return defaultEventDataCallback;
    }

    public EventDataRunner getEventDataRunner(String applicationId) {
        return eventDataRunnerMap.get(applicationId);
    }

    public void putEventDataRunner(String applicationId, EventDataRunner eventDataRunner) {
        this.eventDataRunnerMap.put(applicationId, eventDataRunner);
    }
}
