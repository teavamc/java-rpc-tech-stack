package com.teavamc.rpcgateway.core.flow.limiter.strategy;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 计数器限流器
 *
 * @Package com.teavamc.rpcgateway.core.limiter
 * @date 2021/1/28 上午10:47
 */
public class CountLimiterStrategy extends AbstractLimiterStrategy {

    static final String SCRIPT_FILE_NAME = "count_limiter.lua";

    public CountLimiterStrategy(StringRedisTemplate redisTemplate) {
        super(redisTemplate, SCRIPT_FILE_NAME);
    }

}
