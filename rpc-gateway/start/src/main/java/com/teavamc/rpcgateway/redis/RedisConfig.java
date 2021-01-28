package com.teavamc.rpcgateway.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Package com.teavamc.rpcdatadao.config.redis
 * @date 2021/1/22 下午1:55
 */
@Slf4j
@EnableCaching
@Configurable
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 实例化 Jedis 连接池
     * @return
     */
    @Bean
    public JedisPool jedisPool () {
        // Jedis 连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 拿到工程配置中设置的 redis 参数
        RedisProperties.Pool poolConfig = redisProperties.getJedis().getPool();
        // 最大连接数
        jedisPoolConfig.setMaxTotal(poolConfig.getMaxActive());
        // 最大空闲连接数
        jedisPoolConfig.setMaxIdle(poolConfig.getMaxIdle());
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常,
        // 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(poolConfig.getMaxWait().toMillis());
        // 实例化 Jedis 连接池
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,
                redisProperties.getHost(), redisProperties.getPort(),
                Math.toIntExact(redisProperties.getTimeout().toMillis()),
                redisProperties.getPassword());
        log.info("完成 Redis 连接池的 Bean 初始化");
        return jedisPool;
    }

    /**
     * 使用 fastjson 序列化
     * @return
     */
    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

    /**
     * 对象类型的数据,建议用 RedisTemplate
     * @param redisConnectionFactory
     * @param fastJson2JsonRedisSerializer
     * @return
     */
    @Bean("redisTemplate")
    public RedisTemplate initRedisTemplate(RedisConnectionFactory redisConnectionFactory,
                                           RedisSerializer fastJson2JsonRedisSerializer) {

        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置 Key 与 Value 的序列化模式
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 初始化 bean 必须执行
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 字符串类型的数据,建议用 stringRedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }
}