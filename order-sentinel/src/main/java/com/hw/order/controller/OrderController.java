package com.hw.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hw.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
public class OrderController {

    @RequestMapping("/add")
    public String add() {
        System.out.println("下单成功！");
        return "生成订单";
    }

    @RequestMapping("/get")
    public String get() throws  InterruptedException{
        return "查询订单";
    }

    @RequestMapping("/flow")
//    @SentinelResource(value = "flow", blockHandler = "flowBlockHandler")
    public String flow() {
        return "正常访问";
    }

    public String flowBlockHandler(BlockException e) {
        return "流控";
    }

    @RequestMapping("/flowThread")
    @SentinelResource(value = "flowThread", blockHandler = "flowBlockHandler")
    public String flowThread() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return "正常访问";
    }

    @Autowired
    IOrderService orderService;

    @RequestMapping("/test1")
    public String test1() {
        return orderService.getUser();
    }

    @RequestMapping("/test2")
    public String test2() throws InterruptedException{
        return orderService.getUser();
    }

    // 热点规则：结合SentinelResource使用
    @RequestMapping("/get/{id}")
    @SentinelResource(value = "getById", blockHandler = "HotBlockHandler")
    public String getById(@PathVariable("id") Integer id) throws InterruptedException {
        System.out.println("正常访问");
        return "正常访问";
    }

    public String HotBlockHandler(@PathVariable("id") Integer id, BlockException e) throws InterruptedException {
        return "热点异常处理";
    }
}
