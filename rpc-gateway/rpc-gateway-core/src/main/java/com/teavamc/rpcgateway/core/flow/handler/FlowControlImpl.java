package com.teavamc.rpcgateway.core.flow.handler;

import com.alibaba.fastjson.JSONObject;
import com.teavamc.rpcgateway.core.flow.entity.FlowControlConfig;
import com.teavamc.rpcgateway.core.flow.enums.LimiterEnum;
import com.teavamc.rpcgateway.core.flow.enums.LimiterTimeUnitEnum;
import com.teavamc.rpcgateway.core.flow.limiter.policy.CountLimiterPolicy;
import com.teavamc.rpcgateway.core.flow.limiter.policy.LimiterPolicy;
import com.teavamc.rpcgateway.core.flow.limiter.policy.TokenBucketLimiterPolicy;
import com.teavamc.rpcgateway.core.flow.limiter.strategy.CountLimiterStrategy;
import com.teavamc.rpcgateway.core.flow.limiter.strategy.LimiterStrategy;
import com.teavamc.rpcgateway.core.flow.limiter.strategy.TokenBucketLimiterStrategy;
import com.teavamc.rpcgateway.core.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 流量控制实现类
 *
 * @Package com.teavamc.rpcgateway.core.flow.handler
 * @date 2021/1/28 上午11:48
 */
@Slf4j
@Component
public class FlowControlImpl implements FlowControl {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String PRE_FIX = "gateway:limiter:";

    @Override
    public boolean doFilter(FlowControlConfig flowControlConfig) {
        if (Objects.isNull(flowControlConfig)) {
            log.error("[{}] 流控参数为空", this.getClass().getSimpleName());
            return true;
        }
        String key;
        boolean filterRes = true;
        try {
            key = generateRedisLimiterKey(flowControlConfig);
            LimiterStrategy limiterStrategy = getLimiterStrategyByCode(flowControlConfig.getLimiterType());
            LimiterPolicy limiterPolicy = getLimiterPolicyByCode(flowControlConfig.getLimiterType(), flowControlConfig);
            filterRes = limiterStrategy.access(key, limiterPolicy);
            if (!filterRes) {
                log.warn("Limiter Id:[{}],key :[{}]已达流量上限值:{},被限制请求!", flowControlConfig.getId(), key, flowControlConfig.getValue());
                // todo 接入消息告警
            }
        } catch (Exception e) {
            log.error("[{}] 限流器内部出现异常! 入参:{}", this.getClass().getSimpleName(), JSONObject.toJSON(flowControlConfig));
            e.printStackTrace();
        }
        return !filterRes;
    }

    /**
     * 根据限流器的类别 code 获取限流器对象
     *
     * @param code
     * @return
     */
    private LimiterStrategy getLimiterStrategyByCode(int code) {
        LimiterEnum limiterEnum = LimiterEnum.getLimiterByCode(code);
        switch (Objects.requireNonNull(limiterEnum)) {
            case COUNT:
                return new CountLimiterStrategy(redisTemplate);
            case TOKEN_BUCKET:
                return new TokenBucketLimiterStrategy(redisTemplate);
        }
        throw new RuntimeException("[FlowControlImpl] 限流类型转换时发现未知的限流类型");
    }

    /**
     * 根据限流器的类别 code 获取限流器对象
     *
     * @param code
     * @return
     */
    private LimiterPolicy getLimiterPolicyByCode(int code, FlowControlConfig flowControlConfig) {
        LimiterEnum limiterEnum = LimiterEnum.getLimiterByCode(code);
        long intervalLimiter = TimeUtil.getMillisecond(LimiterTimeUnitEnum.valueOf(flowControlConfig.getTimeUnit()));
        long limiterValue = flowControlConfig.getValue();
        switch (Objects.requireNonNull(limiterEnum)) {
            case COUNT:
                return new CountLimiterPolicy(limiterValue, intervalLimiter);
            case TOKEN_BUCKET:
                // todo 最大突发流量这个暂未实现
                return new TokenBucketLimiterPolicy(limiterValue, intervalLimiter, intervalLimiter);
        }
        throw new RuntimeException("[FlowControlImpl] 限流类型转换时发现未知的限流类型");
    }

    /**
     * 生成分布式限流器的 Key
     *
     * @param controlConfig
     * @return
     */
    private static String generateRedisLimiterKey(FlowControlConfig controlConfig) {
        StringBuilder sb = new StringBuilder();
        final String splitChar = ":";
        // 前缀
        sb.append(PRE_FIX);
        // 添加限流器类型
        String limiterTypeName = LimiterEnum.getLimiterByCode(controlConfig.getLimiterType()).getName();
        sb.append(limiterTypeName).append(splitChar);
        // 限流的ip
        if (!StringUtils.isEmpty(controlConfig.getIp())) {
            sb.append(controlConfig.getIp()).append(splitChar);
        }
        // 限流的维度名称
        if (!StringUtils.isEmpty(controlConfig.getDimensionName())) {
            sb.append(controlConfig.getDimensionName()).append(splitChar)
                .append(controlConfig.getDimensionValue()).append(splitChar);
        }
        return sb.toString();
    }
}
