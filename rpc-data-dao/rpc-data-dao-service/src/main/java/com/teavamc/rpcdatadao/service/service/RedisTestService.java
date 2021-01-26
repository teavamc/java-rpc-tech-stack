package com.teavamc.rpcdatadao.service.service;

/**
 * @Package com.teavamc.rpcdatadao.service.service
 * @date 2021/1/26 下午3:05
 */
public interface RedisTestService {

    /**
     * 获得资源并锁一会儿
     * @return
     */
    boolean GetResourceAndLockSomeAWhile(String  resourceName);
}
