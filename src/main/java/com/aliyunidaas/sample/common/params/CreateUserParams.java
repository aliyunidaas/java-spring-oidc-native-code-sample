package com.aliyunidaas.sample.common.params;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/29 4:24 PM
 * @author: yunqiu
 **/
public class CreateUserParams implements Serializable {

    private String userName;

    private String phoneNumber;

    private String userEmail;

    @NotNull
    private String organizationalUnitId;

    public CreateUserParams(@NotNull String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public @NotNull String getOrganizationalUnitId() {
        return organizationalUnitId;
    }

    public void setOrganizationalUnitId(String organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }
}
