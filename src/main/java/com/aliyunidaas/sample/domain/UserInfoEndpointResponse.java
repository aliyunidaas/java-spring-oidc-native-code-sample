package com.aliyunidaas.sample.domain;

import java.io.Serializable;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/15 3:27 PM
 * @author: yunqiu
 **/
public class UserInfoEndpointResponse implements Serializable {
    /**
     * subject
     */
    private String sub;
    /**
     * 姓名
     */
    private String name;
    /**
     * 别名
     */
    private String preferredUsername;
    /**
     * 邮箱
     */
    private String email;

    public UserInfoEndpointResponse() {
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
