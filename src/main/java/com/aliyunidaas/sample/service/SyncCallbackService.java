package com.aliyunidaas.sample.service;

import com.aliyunidaas.sync.event.bizdata.OrganizationalUnitInfo;
import com.aliyunidaas.sync.event.bizdata.UserInfo;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/7 11:39 PM
 * @author: yunqiu
 **/
public interface SyncCallbackService {
    /**
     * 账户创建
     *
     * @param userInfo
     */
    void userCreateEvent(UserInfo userInfo);

    /**
     * 账户删除
     *
     * @param userInfo
     */
    void userDeleteEvent(UserInfo userInfo);

    /**
     * 账户基础信息更新
     *
     * @param userInfo
     */
    void userUpdateInfoEvent(UserInfo userInfo);

    /**
     * 账户密码更新
     *
     * @param userInfo
     */
    void userUpdatePasswordEvent(UserInfo userInfo);

    /**
     * 账户禁用 (因为Demo示例未实现用户的禁用功能，故该部分不做实现)
     *
     * @param userInfo
     */
    void userDisableEvent(UserInfo userInfo);

    /**
     * 账户启用(因为Demo示例未实现用户的启用功能，故该部分不做实现)
     *
     * @param userInfo
     */
    void userEnableEvent(UserInfo userInfo);

    /**
     * 账户锁定(因为Demo示例未实现用户的锁定功能，故该部分不做实现)
     *
     * @param userInfo
     */
    void userLockEvent(UserInfo userInfo);

    /**
     * 账户解锁(因为Demo示例未实现用户的解锁功能，故该部分不做实现)
     *
     * @param userInfo
     */
    void userUnlockEvent(UserInfo userInfo);

    /**
     * 账户移动
     *
     * @param userInfo
     */
    void userUpdatePrimaryOuEvent(UserInfo userInfo);

    /**
     * 组织创建
     *
     * @param ouInfo
     */
    void ouCreateEvent(OrganizationalUnitInfo ouInfo);

    /**
     * 组织删除
     *
     * @param ouInfo
     */
    void ouDeleteEvent(OrganizationalUnitInfo ouInfo);

    /**
     * 组织更新
     *
     * @param ouInfo
     */
    void ouUpdateEvent(OrganizationalUnitInfo ouInfo);

    /**
     * 组织移动
     *
     * @param ouInfo
     */
    void ouUpdateParentOrganizationalUnitEvent(OrganizationalUnitInfo ouInfo);

    /**
     * 账户推送
     *
     * @param userInfo
     */
    void userPushEvent(UserInfo userInfo);

    /**
     * 组织推送
     *
     * @param ouInfo
     */
    void ouPushEvent(OrganizationalUnitInfo ouInfo);
}
