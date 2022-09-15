package com.aliyunidaas.sample.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/29 5:20 PM
 * @author: yunqiu
 **/
@Entity(name = "ou_info")
@DynamicUpdate
@Table(name = "ou_info",
       indexes = {
               @Index(name = "ou_external_id_index", columnList = "ou_external_id")
       })
public class OuDO {

    @Id
    private String ouId;

    @Column(name="ou_external_id", nullable=false)
    private String ouExternalId;

    private String ouName;

    private String parentOuId;

    public OuDO() {
    }

    public String getOuExternalId() {
        return ouExternalId;
    }

    public void setOuExternalId(String ouExternalId) {
        this.ouExternalId = ouExternalId;
    }

    public String getOuId() {
        return ouId;
    }

    public void setOuId(String organizationalUnitId) {
        this.ouId = organizationalUnitId;
    }

    public String getOuName() {
        return ouName;
    }

    public void setOuName(String organizationalUnitName) {
        this.ouName = organizationalUnitName;
    }

    public String getParentOuId() {
        return parentOuId;
    }

    public void setParentOuId(String parentOrganizationalUnitId) {
        this.parentOuId = parentOrganizationalUnitId;
    }

    @Override
    public String toString() {
        return "OuDO{" +
                "ouId='" + ouId + '\'' +
                ", ouExternalId='" + ouExternalId + '\'' +
                ", ouName='" + ouName + '\'' +
                ", parentOuId='" + parentOuId + '\'' +
                '}';
    }

    public static OuDOBuilder getBuilder() {
        return OuDOBuilder.anOuDO();
    }

    public static final class OuDOBuilder {
        private String ouId;
        private String ouExternalId;
        private String ouName;
        private String parentOuId;

        private OuDOBuilder() {}

        public static OuDOBuilder anOuDO() {return new OuDOBuilder();}

        public OuDOBuilder setOuId(String ouId) {
            this.ouId = ouId;
            return this;
        }

        public OuDOBuilder setOuExternalId(String ouExternalId) {
            this.ouExternalId = ouExternalId;
            return this;
        }

        public OuDOBuilder setOuName(String ouName) {
            this.ouName = ouName;
            return this;
        }

        public OuDOBuilder setParentOuId(String parentOuId) {
            this.parentOuId = parentOuId;
            return this;
        }

        public OuDO build() {
            OuDO ouDO = new OuDO();
            ouDO.setOuId(ouId);
            ouDO.setOuExternalId(ouExternalId);
            ouDO.setOuName(ouName);
            ouDO.setParentOuId(parentOuId);
            return ouDO;
        }
    }
}
