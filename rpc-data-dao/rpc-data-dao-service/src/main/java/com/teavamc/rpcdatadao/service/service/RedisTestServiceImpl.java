package com.teavamc.rpcdatadao.service.service;

import com.teavamc.rpcdatadao.service.utils.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Package com.teavamc.rpcdatadao.service.service
 * @date 2021/1/26 下午3:05
 */
@Slf4j
@Service
public class RedisTestServiceImpl implements RedisTestService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean GetResourceAndLockSomeAWhile(String resourceName) {
        if (StringUtils.isBlank(resourceName)){
            return false;
        }
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(stringRedisTemplate);
        log.info("[{}] 尝试获取资源锁 key:{},",this.getClass().getSimpleName(),resourceName);
        boolean lock_res = redisDistributedLock.tryLock(resourceName,5, TimeUnit.SECONDS);
        // todo 如果 lock_res == true 执行相关获取资源的逻辑
        log.info("[{}] 尝试获取资源锁 key:{} | 结果:{},",this.getClass().getSimpleName(),resourceName,lock_res);
        return lock_res;
    }
}
