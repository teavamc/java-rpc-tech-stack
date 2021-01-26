package com.teavamc.rpcdatadao.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Package com.teavamc.rpcdatadao.service.utils
 * @date 2021/1/26 下午2:28
 */
@Slf4j
public class TestRedisLock {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    RedisDistributedLock redisDistributedLock = new RedisDistributedLock(stringRedisTemplate);

    // 要锁的 key
    final String COMMON_KEY = "redis_lock:demo1resource";

    @Test
    public void testLockDemo(){
        // 创建两个线程
        // 线程 1 先来上锁
        Thread t1 = new Thread(()->{
            // 超时时间
            long timeout = 5;
            // 上锁
            boolean lockRes = redisDistributedLock.tryLock(COMMON_KEY,timeout, TimeUnit.SECONDS);
            log.debug("线程 1 上锁状态:{}",lockRes);
        });
        // 线程 2 来争夺
        Thread t2 = new Thread(()->{
            // 超时时间
            long timeout = 5;
            // 上锁
            boolean lockRes = redisDistributedLock.tryLock(COMMON_KEY,timeout, TimeUnit.SECONDS);
            log.debug("线程 2 上锁状态:{}",lockRes);
        });
        t1.start();
        t2.start();
    }



}
