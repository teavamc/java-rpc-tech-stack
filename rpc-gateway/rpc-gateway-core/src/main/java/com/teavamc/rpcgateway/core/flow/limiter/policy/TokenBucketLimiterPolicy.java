package com.teavamc.rpcgateway.core.flow.limiter.policy;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * 令牌桶限流器的执行对象
 *
 * @Package com.teavamc.rpcgateway.core.limiter.policy
 * @date 2021/1/28 上午11:09
 */
public class TokenBucketLimiterPolicy extends AbstractLimiterPolicy {

    /**
     * 限流时间间隔
     * (重置桶内令牌的时间间隔)
     */
    private final long resetBucketInterval;
    /**
     * 最大令牌数量
     */
    private final long bucketMaxTokens;

    /**
     * 初始可存储数量
     */
    private final long initTokens;

    /**
     * 每个令牌产生的时间
     */
    private final long intervalPerPermit;

    /**
     * 令牌桶对象的构造器
     * @param bucketMaxTokens 桶的令牌上限
     * @param resetBucketInterval 限流时间间隔
     * @param maxBurstTime 最大的突发流量的持续时间(通过计算)
     */
    public TokenBucketLimiterPolicy(long bucketMaxTokens, long resetBucketInterval, long maxBurstTime) {
        // 最大令牌数
        this.bucketMaxTokens = bucketMaxTokens;
        // 限流时间间隔
        this.resetBucketInterval = resetBucketInterval;
        // 令牌的产生间隔 = 限流时间 / 最大令牌数
        intervalPerPermit = resetBucketInterval / bucketMaxTokens;
        // 初始令牌数 = 最大的突发流量的持续时间 / 令牌产生间隔
        // 用 最大的突发流量的持续时间 计算的结果更加合理,并不是每次初始化都要将桶装满
        initTokens = Math.min(maxBurstTime / intervalPerPermit, bucketMaxTokens);
    }

    public long getResetBucketInterval() {
        return resetBucketInterval;
    }

    public long getBucketMaxTokens() {
        return bucketMaxTokens;
    }

    public long getInitTokens() {
        return initTokens;
    }

    public long getIntervalPerPermit() {
        return intervalPerPermit;
    }

    @Override
    public String[] toParams() {
        List<String > list = Lists.newArrayList();
        list.add(String.valueOf(getIntervalPerPermit()));
        list.add(String.valueOf(System.currentTimeMillis()));
        list.add(String.valueOf(getInitTokens()));
        list.add(String.valueOf(getBucketMaxTokens()));
        list.add(String.valueOf(getResetBucketInterval()));
        return list.toArray(new String[]{});
    }

}
