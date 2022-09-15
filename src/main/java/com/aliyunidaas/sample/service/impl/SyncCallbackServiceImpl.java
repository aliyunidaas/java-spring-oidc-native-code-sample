package com.aliyunidaas.sample.service.impl;

import com.aliyunidaas.sample.dao.OuRepository;
import com.aliyunidaas.sample.dao.UserRepository;
import com.aliyunidaas.sample.entity.OuDO;
import com.aliyunidaas.sample.entity.UserDO;
import com.aliyunidaas.sample.service.SyncCallbackService;
import com.aliyunidaas.sync.event.bizdata.OrganizationalUnitInfo;
import com.aliyunidaas.sync.event.bizdata.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/7 11:39 PM
 * @author: yunqiu
 **/
@Component
public class SyncCallbackServiceImpl implements SyncCallbackService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OuRepository ouRepository;

    @Override
    public void userCreateEvent(UserInfo userInfo) {
        UserDO userDO = UserDO.getBuilder()
                              .setUserId(UUID.randomUUID().toString())
                              .setUsername(userInfo.getUsername())
                              .setPhoneRegion(userInfo.getPhoneRegion())
                              .setPhoneNumber(userInfo.getPhoneNumber())
                              .setUserEmail(userInfo.getEmail())
                              .setExternalId(userInfo.getUserId())
                              .setOuId(UUID.randomUUID().toString())
                              .setExternalOuId(userInfo.getPrimaryOrganizationalUnitId())
                              .build();
        userRepository.save(userDO);
    }

    @Override
    public void userDeleteEvent(UserInfo userInfo) {
        userRepository.deleteByExternalId(userInfo.getUserId());
    }

    @Override
    public void userUpdateInfoEvent(UserInfo userInfo) {
        final UserDO userDO = userRepository.findByExternalId(userInfo.getUserId());
        if (null != userDO) {
            UserDO user = UserDO.getBuilder()
                                .setUserId(userDO.getUserId())
                                .setUsername(userInfo.getUsername())
                                .setPhoneRegion(userInfo.getPhoneRegion())
                                .setPhoneNumber(userInfo.getPhoneNumber())
                                .setUserEmail(userInfo.getEmail())
                                .setExternalId(userInfo.getUserId())
                                .setOuId(userInfo.getPrimaryOrganizationalUnitId())
                                .setExternalOuId(userInfo.getPrimaryOrganizationalUnitId())
                                .build();
            userRepository.save(user);
        }
    }

    @Override
    public void userUpdatePasswordEvent(UserInfo userInfo) {

    }

    // 因为Demo示例未实现用户的禁用功能，故该部分不做实现
    @Override
    public void userDisableEvent(UserInfo userInfo) {

    }

    // 因为Demo示例未实现用户的启用功能，故该部分不做实现
    @Override
    public void userEnableEvent(UserInfo userInfo) {

    }

    // 因为Demo示例未实现用户的锁定功能，故该部分不做实现
    @Override
    public void userLockEvent(UserInfo userInfo) {

    }

    // 因为Demo示例未实现用户的解锁功能，故该部分不做实现
    @Override
    public void userUnlockEvent(UserInfo userInfo) {

    }

    @Override
    public void userUpdatePrimaryOuEvent(UserInfo userInfo) {
        UserDO userDO = userRepository.findByExternalId(userInfo.getUserExternalId());
        if (null != userDO) {
            userDO = UserDO.getBuilder()
                           .setUserId(userInfo.getUserExternalId())
                           .setUsername(userInfo.getUsername())
                           .setPhoneRegion(userInfo.getPhoneRegion())
                           .setPhoneNumber(userInfo.getPhoneNumber())
                           .setUserEmail(userInfo.getEmail())
                           .setExternalId(userInfo.getUserId())
                           .setOuId(userDO.getOuId())
                           .setExternalOuId(userInfo.getPrimaryOrganizationalUnitId())
                           .build();
            userRepository.save(userDO);
        }
    }

    @Override
    public void ouCreateEvent(OrganizationalUnitInfo ouInfo) {
        OuDO ouDO = OuDO.getBuilder()
                        .setOuId(UUID.randomUUID().toString())
                        .setOuExternalId(ouInfo.getOrganizationalUnitId())
                        .setOuName(ouInfo.getOrganizationalUnitName())
                        .setParentOuId(ouInfo.getParentId())
                        .build();
        ouRepository.save(ouDO);
    }

    @Override
    public void ouDeleteEvent(OrganizationalUnitInfo ouInfo) {
        ouRepository.deleteByOuExternalId(ouInfo.getOrganizationalUnitId());
    }

    @Override
    public void ouUpdateEvent(OrganizationalUnitInfo ouInfo) {
        final OuDO ouDO = ouRepository.findByOuExternalId(ouInfo.getOrganizationalUnitId());
        if (null != ouDO) {
            OuDO ou = OuDO.getBuilder()
                          .setOuId(ouDO.getOuId())
                          .setOuExternalId(ouInfo.getOrganizationalUnitId())
                          .setOuName(ouInfo.getOrganizationalUnitName())
                          .setParentOuId(ouInfo.getParentId())
                          .build();
            ouRepository.save(ou);
        }
    }

    @Override
    public void ouUpdateParentOrganizationalUnitEvent(OrganizationalUnitInfo ouInfo) {
        final OuDO ouDO = ouRepository.findByOuExternalId(ouInfo.getOrganizationalUnitId());
        if (null != ouDO) {
            OuDO ou = OuDO.getBuilder()
                          .setOuId(ouDO.getOuId())
                          .setOuExternalId(ouInfo.getOrganizationalUnitId())
                          .setOuName(ouInfo.getOrganizationalUnitName())
                          .setParentOuId(ouInfo.getParentId())
                          .build();
            ouRepository.save(ou);
        }
    }

    @Override
    public void userPushEvent(UserInfo userInfo) {
        // 需要对比数据库 (saveOrUpdate)
        final UserDO userDO = userRepository.findByExternalId(userInfo.getUserId());
        UserDO user;
        if (null == userDO) {
            // 保存
            user = UserDO.getBuilder()
                         .setUserId(UUID.randomUUID().toString())
                         .setUsername(userInfo.getUsername())
                         .setPhoneRegion(userInfo.getPhoneRegion())
                         .setPhoneNumber(userInfo.getPhoneNumber())
                         .setUserEmail(userInfo.getEmail())
                         .setExternalId(userInfo.getUserId())
                         .setOuId(UUID.randomUUID().toString())
                         .setExternalOuId(userInfo.getPrimaryOrganizationalUnitId())
                         .build();
        } else {
            // 更新
            user = UserDO.getBuilder()
                         .setUserId(userDO.getExternalId())
                         .setUsername(userInfo.getUsername())
                         .setPhoneRegion(userInfo.getPhoneRegion())
                         .setPhoneNumber(userInfo.getPhoneNumber())
                         .setUserEmail(userInfo.getEmail())
                         .setExternalId(userInfo.getUserId())
                         .setOuId(userDO.getOuId())
                         .setExternalOuId(userInfo.getPrimaryOrganizationalUnitId())
                         .build();
        }
        userRepository.save(user);
    }

    @Override
    public void ouPushEvent(OrganizationalUnitInfo ouInfo) {
        // 需要对比数据库 (saveOrUpdate)
        final OuDO ouDO = ouRepository.findByOuExternalId(ouInfo.getOrganizationalUnitId());
        OuDO ou;
        if (null == ouDO) {
            ou = OuDO.getBuilder()
                     .setOuId(UUID.randomUUID().toString())
                     .setOuExternalId(ouInfo.getOrganizationalUnitId())
                     .setOuName(ouInfo.getOrganizationalUnitName())
                     .setParentOuId(ouInfo.getParentId())
                     .build();
        } else {
            ou = OuDO.getBuilder()
                     .setOuId(ouDO.getOuId())
                     .setOuExternalId(ouInfo.getOrganizationalUnitId())
                     .setOuName(ouInfo.getOrganizationalUnitName())
                     .setParentOuId(ouInfo.getParentId())
                     .build();
        }
        ouRepository.save(ou);
    }

}
