package com.hw.order.feign;

import org.springframework.stereotype.Component;

@Component
public class StockFeignServiceFallback implements StockFeignService{
    @Override
    public String reduct2() {
        return "降级啦！！！";
    }
}
