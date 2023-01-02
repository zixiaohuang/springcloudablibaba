package com.hw.sentinelnew.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.hw.sentinelnew.pojo.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

// 流控规则 -》 服务提供方 降级-》服务消费方
@RestController
@Slf4j
public class HelloController {

    private static final String RESOURCE_NAME = "hello";
    private static final String USER_RESOURCE_NAME = "user";
    private static final String DEGRADE_RESOURCE_NAME = "degrade";

    // 进行sentinel流控
    @RequestMapping(value = "/hello")
    public String hello() {
        // 侵入性太强，耦合到接口里面了
        Entry entry = null;
        try {
            entry = SphU.entry(RESOURCE_NAME);
            String str = "hello world";
            log.info("======" + str + "=======");
            return str;
        }catch (BlockException el) {
            log.info("block");
            return "被流控了!";
        }catch (Exception ex) {
            Tracer.traceEntry(ex, entry);
        } finally {
            if(entry != null) {
                entry.exit();
            }
        }
        return null;
    }

    // 初始化方法
    @PostConstruct
    private static void initFlowRules() {
        // 流控规则
        List<FlowRule> rules = new ArrayList<>();
        //流控
        FlowRule rule = new FlowRule();
        //设置受保护的资源
        rule.setResource(RESOURCE_NAME);
        //设置流控规则QPS
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置受保护的资源阈值
        rule.setCount(1);
        rules.add(rule);

        //流控
        FlowRule rule2 = new FlowRule();
        //设置受保护的资源
        rule2.setResource(RESOURCE_NAME);
        //设置流控规则QPS
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule2.setCount(1);
        rules.add(rule2);

        //加载配置好的规则
        FlowRuleManager.loadRules(rules);
    }

    @PostConstruct
    public void initDegradeRule() {
        /* 降级规则  异常*/
        List<DegradeRule> degradeRules = new ArrayList<>();
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource(DEGRADE_RESOURCE_NAME);
        // 设置规则：异常数
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        // 触发熔断异常数： 2
        degradeRule.setCount(2);
        // 触发熔断最小请求数：2
        degradeRule.setMinRequestAmount(2);
        // 统计时长：1分钟
        degradeRule.setStatIntervalMs(60*1000); // 时间太短不好测

        //熔断持续时长
        degradeRule.setTimeWindow(10);

        // 一分钟内， 执行了2次， 出现了2次异常 就会触发熔断

        degradeRules.add(degradeRule);
        DegradeRuleManager.loadRules(degradeRules);
    }

    // SentinelResource使用
    // 1. 添加sentinel-annotation-aspectj依赖
    // 2. 配置bean SentinelResourceAspect
    // value定义资源 blockHandle 设置流控降级后的处理方法（默认方法必须声明在同一个类）fallback当接口出现了异常，可以交给fallback指定的方法进行处理
    @RequestMapping("/user")
    @SentinelResource(value = USER_RESOURCE_NAME, blockHandler="blockHandlerForGetUser")
    public User getUser(String id) {
        return new User("hero");
    }

    // 1.一定要是public
    // 2。返回值跟原方法保持一致，包含原方法参数
    // 3。可以在参数最后添加BlockException
    public User blockHandlerForGetUser(String id, BlockException ex) {
        ex.printStackTrace();
        return new User("流控!!");
    }
}
