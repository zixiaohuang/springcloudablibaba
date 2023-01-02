package com.hw.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="product-service", path = "/product")
public interface ProductFeignService {

    @RequestMapping("/{id}")
    String get(@PathVariable("id") Integer id);


}
