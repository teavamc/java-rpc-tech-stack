package com.teavamc.rpcgateway.core.flow.limiter.strategy;

import com.teavamc.rpcgateway.core.flow.enums.LimiterEnum;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 令牌桶限流器
 *
 * @Package com.teavamc.rpcgateway.core.limiter
 * @date 2021/1/28 上午10:46
 */
public class TokenBucketLimiterStrategy extends AbstractLimiterStrategy {

    static final String SCRIPT_FILE_NAME = "tokenbucket_limiter.lua";

    public TokenBucketLimiterStrategy(StringRedisTemplate redisTemplate) {
        super(redisTemplate, SCRIPT_FILE_NAME);
    }

}
