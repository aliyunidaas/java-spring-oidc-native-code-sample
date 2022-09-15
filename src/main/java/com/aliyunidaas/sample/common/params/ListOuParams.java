package com.aliyunidaas.sample.common.params;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/17 5:09 PM
 * @author: yunqiu
 **/
public class ListOuParams {

    private String parentId;

    private Integer pageNumber = 1;

    private Integer pageSize = 20;

    public ListOuParams(String parentId, Integer pageNumber, Integer pageSize) {
        this.parentId = parentId;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public ListOuParams() {
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
