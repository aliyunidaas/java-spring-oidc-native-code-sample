package com.aliyunidaas.sample.controller;

import com.aliyunidaas.sample.common.config.InitConfiguration;
import com.aliyunidaas.sample.service.SyncService;
import com.aliyunidaas.sync.event.objects.RequestObject;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/28 4:17 PM
 * @author: yunqiu
 **/
@RestController
public class SyncController {

    @Autowired
    private SyncService syncService;

    @Autowired
    private InitConfiguration initConfiguration;

    /**
     * 接收来自IDaaS应用发起的请求，并将结果解析存到数据库内
     * @param callbackRequestObject 事件回调信息
     * @throws JoseException 异常
     */
    @RequestMapping(value = "/receive_sync", method = RequestMethod.POST)
    @ResponseBody
    public void receiveSync(@RequestBody RequestObject callbackRequestObject) throws JoseException {
        syncService.receiveSync(callbackRequestObject);
    }

    /**
     * 通过developAPI拉取IDaaS某个节点下的所有信息（用户信息+组织机构信息），并存储在demo应用的数据库中
     *
     * 注意：在同步过程中避免组织机构以及用户的变更，如移动组织，移动用户
     */
    @GetMapping("/trigger_sync")
    public void triggerSync() {
        syncService.triggerSync(initConfiguration.getInstanceId(), initConfiguration.getApplicationId());
    }
}
