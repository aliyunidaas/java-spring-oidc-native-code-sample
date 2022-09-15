package com.aliyunidaas.sample.common.params;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/17 5:17 PM
 * @author: yunqiu
 **/
public class CreateOuParams {

    private String ouExternalId;

    private String ouName;

    private String parentId;

    public CreateOuParams(String ouExternalId, String ouName, String parentId) {
        this.ouExternalId = ouExternalId;
        this.ouName = ouName;
        this.parentId = parentId;
    }

    public String getOuExternalId() {
        return ouExternalId;
    }

    public void setOuExternalId(String ouExternalId) {
        this.ouExternalId = ouExternalId;
    }

    public String getOuName() {
        return ouName;
    }

    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
