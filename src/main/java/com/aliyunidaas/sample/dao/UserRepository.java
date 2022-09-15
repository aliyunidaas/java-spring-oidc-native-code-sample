package com.aliyunidaas.sample.dao;

import com.aliyunidaas.sample.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/8/1 11:37 AM
 * @author: yunqiu
 **/

@Repository
public interface UserRepository extends JpaRepository<UserDO, String> {

    void deleteByExternalId(String externalId);

    UserDO findByExternalId(String externalId);

}
