package com.teavamc.rpcgateway.core.flow.limiter.policy;

import com.google.common.collect.Lists;
import com.teavamc.rpcgateway.core.flow.enums.LimiterEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 计数器限流器
 *
 * @Package com.teavamc.rpcgateway.core.limiter.policy
 * @date 2021/1/28 上午11:07
 */
public class CountLimiterPolicy extends AbstractLimiterPolicy {

    /**
     * 最大令牌数量
     */
    private final long capacity;

    /**
     * 限流时间间隔 (millsecond)
     */
    private final long intervalInMills;

    public CountLimiterPolicy(long capacity, long intervalInMills) {
        this.capacity = capacity;
        this.intervalInMills = intervalInMills;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getIntervalInMills() {
        return intervalInMills;
    }

    @Override
    public String[] toParams() {
        List<String> list = Lists.newArrayList();
        list.add(String.valueOf(getIntervalInMills()));
        list.add(String.valueOf(System.currentTimeMillis()));
        list.add(String.valueOf(getCapacity()));
        return list.toArray(new String[]{});
    }

}
