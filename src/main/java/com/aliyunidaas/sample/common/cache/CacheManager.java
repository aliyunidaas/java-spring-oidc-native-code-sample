package com.aliyunidaas.sample.common.cache;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/6 5:08 PM
 * @author: longqiuling
 **/
public interface CacheManager {
    /**
     * set cache
     *
     * @param cacheKey 缓存key
     * @param cacheValue 缓存value
     */
    void setCache(String cacheKey, Object cacheValue);

    /**
     * get cache
     *
     * @param cacheKey 缓存key
     * @return value
     */
    <T> T getCache(String cacheKey);

}
