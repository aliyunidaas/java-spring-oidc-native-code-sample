package com.aliyunidaas.sample.common.params;

import java.io.Serializable;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/3 5:32 PM
 * @author: yunqiu
 **/
public class ListUsersParams implements Serializable {

    public String organizationalUnitId = "ou_root";

    public Integer pageNumber = 1;

    public Integer pageSize = 20;

    public ListUsersParams() {
    }

    public ListUsersParams(String organizationalUnitId, Integer pageNumber, Integer pageSize) {
        this.organizationalUnitId = organizationalUnitId;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public String getOrganizationalUnitId() {
        return organizationalUnitId;
    }

    public void setOrganizationalUnitId(String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
