package com.aliyunidaas.sample.common.config;

import com.aliyunidaas.sample.common.SimpleAuthnInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/6/23 7:25 PM
 * @author: longqiuling
 **/
@Configuration
@EnableWebMvc
public class WebAppConfigurer implements WebMvcConfigurer {

    @Bean
    SimpleAuthnInterceptor simpleAuthnInterceptor() {
        return new SimpleAuthnInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(simpleAuthnInterceptor())
                .addPathPatterns("/token/**");
    }
}
