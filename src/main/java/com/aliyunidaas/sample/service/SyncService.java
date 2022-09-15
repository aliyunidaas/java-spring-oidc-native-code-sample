package com.aliyunidaas.sample.service;

import com.aliyunidaas.sync.event.objects.RequestObject;
import com.aliyunidaas.sync.event.objects.ResponseObject;
import org.jose4j.lang.JoseException;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/5 2:05 PM
 * @author: yunqiu
 **/

public interface SyncService {
    /**
     * 账户同步，接收来自IDaaS的数据
     *
     * @param callbackRequestObject
     * @return 事件推送相应
     * @throws JoseException
     */
    ResponseObject receiveSync(RequestObject callbackRequestObject) throws JoseException;

    /**
     * 通过developAPI拉取IDaaS某个节点下的所有信息（用户信息+组织机构信息），并存储在demo应用的数据库中
     *
     * @param instanceId    实例ID
     * @param applicationId 应用ID
     */
    void triggerSync(String instanceId, String applicationId);
}
