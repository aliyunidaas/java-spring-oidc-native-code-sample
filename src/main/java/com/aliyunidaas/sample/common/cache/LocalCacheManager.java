package com.aliyunidaas.sample.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:  This project is a demo example. It is recommended to use cache middleware (redis) for storage in the project
 *
 * @date: 2022/6/30 10:11 AM
 * @author: longqiuling
 **/
@Configuration
public class LocalCacheManager implements CacheManager {

    private final Cache<String, Object> cache = CacheBuilder
            .newBuilder()
            //设置cache的初始大小
            .initialCapacity(20)
            //并发数
            .concurrencyLevel(2)
            //过期时间
            .expireAfterWrite(3600, TimeUnit.SECONDS)
            .build();

    /**
     * add cache
     *
     * @param cacheKey
     * @param cacheValue
     */
    @Override
    public void setCache(String cacheKey, Object cacheValue) {
        if (cacheValue == null) {
            return;
        }
        cache.put(cacheKey, cacheValue);
    }

    /**
     * get cache
     *
     * @param cacheKey
     * @return value
     */
    @Override
    public <T> T getCache(String cacheKey) {
        return (T)cache.getIfPresent(cacheKey);
    }

}
