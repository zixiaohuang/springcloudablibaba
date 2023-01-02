package com.hw.order.config;

import com.hw.order.intercptor.feign.CustomFeignIntercptor;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *  全局配置： 当使用Configuration 会将配置作用所有的服务提供方
 *  局部配置： 1. 通过配置类不加Configuration
 *           2. 通过配置文件
 * */

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }


//    // 自定义拦截器
//    @Bean
//    public CustomFeignIntercptor feignAuthRequestInterceptor() {
//        return new CustomFeignIntercptor();
//    }


}
