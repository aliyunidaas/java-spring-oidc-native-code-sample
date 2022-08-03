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
     * 设置缓存
     *
     * @param cacheKey 缓存key
     * @param cacheValue 缓存value
     */
    void setCache(String cacheKey, Object cacheValue);

    /**
     * 获取缓存
     *
     * @param cacheKey 缓存key
     * @return value
     */
    <T> T getCache(String cacheKey);

}
