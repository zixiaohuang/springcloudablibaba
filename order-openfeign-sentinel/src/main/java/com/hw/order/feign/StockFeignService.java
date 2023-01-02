package com.hw.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/*

2. 添加fegin接口和方法
* name 指定调用rest接口所对应的服务名
* path 指定调用rest接口所在的StockController指定的@RequestMapping
*
* */
@FeignClient(name = "stock-service", path = "/stock", fallback = StockFeignServiceFallback.class)
public interface StockFeignService {

    // 声明需要调用的rest接口对应的方法
    @RequestMapping("/reduct2")
    String reduct2();

}

/*
*
@RequestMapping("/stock")
public class StockController {

    @Value("${server.port}")
    String port;

    @RequestMapping("/reduct")
    public String reduct() {
        System.out.println("扣减库存");
        return "扣减库存" + port;
    }
}
*
*
* */