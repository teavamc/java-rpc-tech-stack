package com.teavamc.rpcgateway.core.flow.limiter.standalone;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单机计数器
 *
 * @Package com.teavamc.rpcgateway.core.flow.limiter.standalone
 * @date 2021/2/1 上午11:01
 */
@Slf4j
@Component
public class StandAloneCountLimiter implements StandAloneLimiter {

    // 计数器限流器
    Map<String, CountLimiter> countLimiterMap = new ConcurrentHashMap<>();

    /**
     * 流量是否通过
     *
     * @param key
     * @param maxLimiter
     * @param interval
     * @return
     */
    public boolean access(String key, Long maxLimiter, Long interval) {
        if (StringUtils.isBlank(key) || Objects.isNull(maxLimiter) || Objects.isNull(interval)) {
            log.error("[{}]限流参数不能为空 - key:{}, maxLimiter:{}, interval:{}", this.getClass().getSimpleName(), key, maxLimiter, interval);
            return false;
        }
        Long currTime = System.currentTimeMillis();
        Long limiterRemainTokens = getLimiterTokens(key, maxLimiter, interval, currTime);
        return limiterRemainTokens > 0;
    }

    /**
     * @param key
     * @param maxLimiter
     * @param interval
     * @return
     */
    private Long getLimiterTokens(String key, Long maxLimiter, Long interval, Long currTime) {
        if (StringUtils.isBlank(key)) {
            log.error("[{}]获取本地限流器中的 key 为空.", this.getClass().getSimpleName());
            return null;
        }
        CountLimiter currLimiter;

        // 判断限流器是否存在
        if (countLimiterMap.containsKey(key)) {
            currLimiter = countLimiterMap.get(key);
            // 超过间隔时间就重置
            if (currTime - currLimiter.getStartTime() > currLimiter.interval) {
                currLimiter.setCurrentTokens(maxLimiter);
                // 更新区间时间为上一个区间开始时间 = 当前时间 - 限流间隔(偏移量)
                currLimiter.setStartTime(currTime - interval);
            }
        } else {
            // 不存在就初始化
            currLimiter = initLimiter(key, maxLimiter, interval);
        }

        // 处理是否可通过
        Long remainTokens = currLimiter.getCurrentTokens();
        if (remainTokens > 0) {
            currLimiter.setCurrentTokens(remainTokens - 1);
            return remainTokens;
        } else {
            currLimiter.setCurrentTokens(0L);
            return 0L;
        }
    }

    // 六个月的时间,从对引擎和相关业务域的一概不通,到现在了解引擎业务并熟悉流程

    /**
     * 初始化限流器
     *
     * @param key        限流器的 key
     * @param maxLimiter
     * @param interval
     * @return
     */
    private CountLimiter initLimiter(String key, Long maxLimiter, Long interval) {
        // 确定 map 中不存在
        if (!countLimiterMap.containsKey(key)) {
            CountLimiter limiter = new CountLimiter(key, maxLimiter, interval);
            countLimiterMap.put(key, limiter);
            return limiter;
        } else {
            // 存在
            log.error("[{}]存在 key 为:{}的限流器!不进行初始化,返回存在的限流器.", this.getClass().getSimpleName(), key);
            return countLimiterMap.get(key);
        }
    }

    @Data
    static class CountLimiter implements Serializable {

        private static final long serialVersionUID = -3326978639364146093L;

        // 限流器的 key
        private final String key;

        // 最大数量
        private final Long maxTokens;

        // 每个限流区间的间隔
        private final Long interval;

        // 当前令牌数
        private volatile Long currentTokens;

        // 当前区间的开始时间
        private volatile Long startTime;

        public CountLimiter(String key, Long maxLimiter, Long interval) {
            this.key = key;
            this.maxTokens = maxLimiter;
            this.interval = interval;
            // 初始化的当前值 = 初始化值
            this.currentTokens = maxTokens;
            // 当前的开始时间
            this.startTime = System.currentTimeMillis();
        }

    }

}
