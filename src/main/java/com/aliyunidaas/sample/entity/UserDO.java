package com.aliyunidaas.sample.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/29 5:20 PM
 * @author: yunqiu
 **/
@Entity(name = "user_info")
@DynamicUpdate
@Table(name = "user_info",
       indexes = {
        @Index(name = "external_id_index", columnList = "external_id")
})
public class UserDO implements Serializable {

    @Id
    private String userId;

    private String username;

    private String phoneRegion;

    private String phoneNumber;

    private String userEmail;

    @Column(name="external_id", nullable=false)
    private String externalId;

    private String ouId;

    private String externalOuId;

    public UserDO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getOuId() {
        return ouId;
    }

    public void setOuId(String ouId) {
        this.ouId = ouId;
    }

    public String getExternalOuId() {
        return externalOuId;
    }

    public void setExternalOuId(String externalOuId) {
        this.externalOuId = externalOuId;
    }

    public String getPhoneRegion() {
        return phoneRegion;
    }

    public void setPhoneRegion(String phoneRegion) {
        this.phoneRegion = phoneRegion;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", phoneRegion='" + phoneRegion + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", externalId='" + externalId + '\'' +
                ", ouId='" + ouId + '\'' +
                ", externalOuId='" + externalOuId + '\'' +
                '}';
    }

    public static UserDOBuilder getBuilder() {
        return UserDOBuilder.anUserDO();
    }

    public static final class UserDOBuilder {
        private String userId;
        private String username;
        private String phoneRegion;
        private String phoneNumber;
        private String userEmail;
        private String externalId;
        private String ouId;
        private String externalOuId;

        private UserDOBuilder() {}

        public static UserDOBuilder anUserDO() {return new UserDOBuilder();}

        public UserDOBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public UserDOBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserDOBuilder setPhoneRegion(String phoneRegion) {
            this.phoneRegion = phoneRegion;
            return this;
        }

        public UserDOBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserDOBuilder setUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public UserDOBuilder setExternalId(String externalId) {
            this.externalId = externalId;
            return this;
        }

        public UserDOBuilder setOuId(String ouId) {
            this.ouId = ouId;
            return this;
        }

        public UserDOBuilder setExternalOuId(String externalOuId) {
            this.externalOuId = externalOuId;
            return this;
        }

        public UserDO build() {
            UserDO userDO = new UserDO();
            userDO.setUserId(userId);
            userDO.setUsername(username);
            userDO.setPhoneRegion(phoneRegion);
            userDO.setPhoneNumber(phoneNumber);
            userDO.setUserEmail(userEmail);
            userDO.setExternalId(externalId);
            userDO.setOuId(ouId);
            userDO.setExternalOuId(externalOuId);
            return userDO;
        }
    }
}
