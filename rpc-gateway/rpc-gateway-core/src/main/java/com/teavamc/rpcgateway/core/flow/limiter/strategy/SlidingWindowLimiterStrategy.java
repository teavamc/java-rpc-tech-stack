package com.teavamc.rpcgateway.core.flow.limiter.strategy;

import com.teavamc.rpcgateway.core.flow.enums.LimiterEnum;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 滑动窗口限流器
 *
 * @Package com.teavamc.rpcgateway.core.limiter
 * @date 2021/1/28 上午10:47
 */
public class SlidingWindowLimiterStrategy extends AbstractLimiterStrategy {
    /**
     * 抽象父类限流器的构造器
     *
     * @param redisTemplate
     * @param scriptName
     */
    public SlidingWindowLimiterStrategy(StringRedisTemplate redisTemplate, String scriptName) {
        super(redisTemplate, scriptName);
    }
}
