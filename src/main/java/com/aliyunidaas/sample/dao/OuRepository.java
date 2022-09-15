package com.aliyunidaas.sample.dao;

import com.aliyunidaas.sample.entity.OuDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuRepository extends JpaRepository<OuDO, String> {

    void deleteByOuExternalId(String ouExternalId);

    OuDO findByOuExternalId(String ouExternalId);
}
