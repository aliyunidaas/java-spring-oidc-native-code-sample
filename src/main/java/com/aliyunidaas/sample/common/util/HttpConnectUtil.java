package com.aliyunidaas.sample.common.util;

import com.alibaba.fastjson.JSON;
import com.aliyunidaas.sample.exception.BizException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/5 2:51 PM
 * @author: longqiuling
 **/
@Component
public class HttpConnectUtil {
    /**
     * a Get request without parameters
     *
     * @param endpoint 请求端点
     * @return 请求返回值
     */
    public String doGetConnect(String endpoint) throws IOException {
        HttpGet httpGet = new HttpGet(endpoint);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            if (isFail(response)) {
                responseError(result);
            }
            return result;
        } catch (BizException bizException) {
            throw new BizException(bizException.getError(), bizException.getDescription());
        }

    }

    /**
     * a Post request with parameters
     *
     * @param httpPost 请求体
     * @return 请求返回值
     */
    public String doPostConnect(HttpPost httpPost) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            if (isFail(response)) {
                responseError(result);
            }
            return result;
        } catch (BizException bizException) {
            throw new BizException(bizException.getError(), bizException.getDescription());
        }
    }

    /**
     * 根据状态码判断请求是否失败
     * @param response
     * @return
     */
    private boolean isFail(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() != 200;
    }

    /**
     * 解析失败请求的原因
     * @param result
     */
    public void responseError(String result) {
        BizException bizException = JSON.parseObject(result, BizException.class);
        if (bizException.getError() != null) {
            throw new BizException(bizException.getError(), bizException.getDescription());
        }
    }
}
