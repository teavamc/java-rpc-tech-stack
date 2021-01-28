package com.teavamc.rpcgateway.core.flow.limiter.strategy;


import com.teavamc.rpcgateway.core.flow.limiter.policy.LimiterPolicy;

import java.util.List;

/**
 * 限流器
 * @Package com.teavamc.rpcgateway.core.limiter
 * @date 2021/1/26 下午5:35
 */
public interface LimiterStrategy {

    /**
     * 判断这个 key 用什么策略[LimiterPolicy]进行限流,
     * 返回是否应该通过
     *
     * @param key
     * @return
     */
    boolean access (String key, LimiterPolicy policy);

    boolean access (String key, String... args);

}
