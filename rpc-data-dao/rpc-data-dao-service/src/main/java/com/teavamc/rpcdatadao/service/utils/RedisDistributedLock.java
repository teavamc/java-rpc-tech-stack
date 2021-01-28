package com.teavamc.rpcdatadao.service.utils;

import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Package com.teavamc.rpcdatadao.service.utils
 * @date 2021/1/22 下午2:52
 */
@Slf4j
public class RedisDistributedLock {

    private StringRedisTemplate redisTemplate;

    /**
     * 重试等待时间
     */
    private int retryAwait = 200;

    /**
     * redis key 的过期时间 seconds
     */
    public static final long MAX_EXPIRE_TIME = 10;


    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 阻塞式分布式锁
     *
     * @param key
     * @param timeout 超时时间
     * @param unit    超时单位
     * @return
     */
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        log.info("[Redis Lock] 尝试获取阻塞式分布式锁 -- start time:{} -- key :{}", System.currentTimeMillis(), key);
        // 获取系统此刻的时间,用毫秒来返回
        final long startMillis = System.currentTimeMillis();
        // 转换参数中的 timeout 为毫秒并返回
        final Long millisToWait = unit.toMillis(timeout);
        // 分布式锁的值
        boolean lockValue;
        // 上锁成功立即返回,上锁失败就直到等待超时还未上锁返回
        int failed_cnt = 0;
        while (true) {
            // 拿到上锁结果 MAX_EXPIRE_TIME = 10s
            lockValue = lock(key, MAX_EXPIRE_TIME, TimeUnit.SECONDS);
            // 若上锁成功,就返回
            if (lockValue) {
                log.info("[Redis Lock] 尝试获取阻塞式分布式锁 | 成功 -- key :{}", key);
                return true;
            }
            // 超时的判断 : 当前时间 - 开始时间 - 重试等待时间 > 超时时间
            Long waitDiffTime = System.currentTimeMillis() - startMillis - retryAwait;
            // 若上锁不成功,判断是否超时,若超时就返回
            if (waitDiffTime > millisToWait) {
                // 超时返回, 上锁失败
                log.warn("[Redis Lock] 尝试获取阻塞式分布式锁{}次,耗时{}ms | 最终超时失败 -- key :{}"
                        , failed_cnt,waitDiffTime, key);
                return false;
            }
            log.info("[Redis Lock] 尝试获取阻塞式分布式锁 | 失败{}次, 尝试超时等待:{}ms 后重新发起获取 -- key :{}",
                    ++failed_cnt, retryAwait, key);
            // 阻塞当前线程, 阻塞时长是重试等待时间
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryAwait));
        }
    }

    /**
     * 无阻塞分布式锁
     *
     * @param key
     * @param expireTime 过期时间
     * @param unit 时间单位
     * @return
     */
    public boolean lock(String key, long expireTime, TimeUnit unit) {
        // key 和 value 的序列化方式
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        final String SET = "SET";
        byte[] key_cmd = keySerializer.serialize(key);
        byte[] value_cmd = valueSerializer.serialize("1");
        final byte[] NX = encode("NX");
        final byte[] EX = encode("EX");
        byte[] expireTime_cmd = encode(String.valueOf(unit.toSeconds(expireTime)));

        // 例 : set "key" "1" NX EX 3000
        // 如果 key - "key" 不存在就设置, value 设置成 "1", 若存在就设置失败
        return redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.execute(SET, key_cmd, value_cmd, NX, EX, expireTime_cmd) != null);
    }

    /**
     * 解锁
     * 删除 key
     *
     * @param key
     */
    public void unlock(String key) {
        redisTemplate.delete(key);
    }

    /**
     * String 转 Byte[]
     *
     * @param str
     * @return
     */
    public static byte[] encode(final String str) {
        try {
            // 当前 str 的格式是 UTF-8
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // 不支持的字符编码异常
            throw new RedisException(e);
        }
    }
}
