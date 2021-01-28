package com.teavamc.rpcgateway.web;

import com.teavamc.rpcgateway.core.flow.entity.FlowControlConfig;
import com.teavamc.rpcgateway.core.flow.handler.FlowControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Package com.teavamc.rpcgateway.web
 * @date 2021/1/26 下午5:27
 */
@Slf4j
@RestController
@RequestMapping(value = "/flowControl")
public class TestRateLimiterController {

    @Resource
    private FlowControl flowControl;

    // api 请求次数
    private Map<Long, Integer> apiRequestCount = new ConcurrentHashMap<>();
    // api 成功请求
    private Map<Long, Integer> apiRequestSuccessCount = new ConcurrentHashMap<>();
    // api 失败请求
    private Map<Long, Integer> apiRequestFailedCount = new ConcurrentHashMap<>();


    @PostMapping(value = "/test")
    public void testFlowControl(@RequestBody FlowControlConfig controlConfig) {
        Long apiId = controlConfig.getId();
        log.info("接收到 ApiId :{} 的请求", apiId);
        apiRequestCount.put(apiId, apiRequestCount.getOrDefault(apiId, 0) + 1);
        // 执行限流
        boolean res = flowControl.doFilter(controlConfig);
        if (res) {
            apiRequestFailedCount.put(apiId, apiRequestFailedCount.getOrDefault(apiId, 0) + 1);
        } else {
            apiRequestSuccessCount.put(apiId, apiRequestSuccessCount.getOrDefault(apiId, 0) + 1);
        }
        // 处理结果
        int totalCnt = apiRequestCount.get(apiId);
        int successCnt = apiRequestSuccessCount.get(apiId) == null ? 0 : apiRequestSuccessCount.get(apiId);
        int failedCnt = apiRequestFailedCount.get(apiId) == null ? 0 : apiRequestFailedCount.get(apiId);
        log.info(" ApiId :{} 的请求是否被限流:{} | 共请求{}次,放行{}次,限流{}次", apiId, res, totalCnt, successCnt, failedCnt);
    }

}
