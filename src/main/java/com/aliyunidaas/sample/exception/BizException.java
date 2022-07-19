package com.aliyunidaas.sample.exception;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/4 10:26 AM
 * @author: longqiuling
 **/
public class BizException extends RuntimeException {

    private String error;

    private String description;

    public BizException() {

    }

    public BizException(String error, String description) {
        super();
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}