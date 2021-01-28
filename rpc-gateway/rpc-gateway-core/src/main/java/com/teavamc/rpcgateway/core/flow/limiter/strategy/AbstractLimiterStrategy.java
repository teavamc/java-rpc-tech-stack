package com.teavamc.rpcgateway.core.flow.limiter.strategy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.teavamc.rpcgateway.core.flow.enums.LimiterEnum;
import com.teavamc.rpcgateway.core.flow.limiter.policy.LimiterPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * 限流器的抽象父类
 *
 * @Package com.teavamc.rpcgateway.core.limiter
 * @date 2021/1/27 上午10:47
 */
@Slf4j
public abstract class AbstractLimiterStrategy implements LimiterStrategy {

    // redis 代理
    private StringRedisTemplate redisTemplate;

    // 转成 redis 的脚本
    private static RedisScript<Long> redisScript;

    // lua 脚本名称
    private String scriptName;

    // lua 脚本内容
    private String script;

    /**
     * 抽象父类限流器的构造器
     *
     * @param redisTemplate
     * @param scriptName
     */
    public AbstractLimiterStrategy(StringRedisTemplate redisTemplate, String scriptName) {
        Preconditions.checkNotNull(scriptName, "scriptName is null");
        Preconditions.checkNotNull(redisTemplate, "RedisTemplate is null");
        this.redisTemplate = redisTemplate;
        this.scriptName = scriptName;
        init();
    }

    /**
     * 初始化限流器
     */
    private void init() {
        try {
            // 构建获取 lua 脚本的脚本
            // classpath:limiter/ + 脚本名称, 因为是两种限流模式,所以有两个
            // 计数器 classpath:limiter/count_limiter.lua
            // 令牌桶 classpath:limiter/tokenbucket_limiter.lua
            // 获取资源
            this.script = read("classpath:limiter/" + this.scriptName);
            redisScript = new DefaultRedisScript(this.script, Long.class);
        } catch (IOException e) {
            log.error("read script error ", e);
        }
    }

    /**
     * 获取资源
     *
     * @param luaScriptPath
     * @return
     * @throws IOException
     */
    private String read(String luaScriptPath) throws IOException {
        // Spring 的资源查找器, 可以通过通配符的方式查找资源
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        // 获取资源下的所有文件
        Resource[] resources = patternResolver.getResources(luaScriptPath);
        // 找到资源, 直接获取第一份
        if (resources != null && resources.length > 0) {
            InputStream inputStream = resources[0].getInputStream();
            byte[] scriptBytes = ByteStreams.toByteArray(inputStream);
            return new String(scriptBytes);
        }
        return null;
    }

    @Override
    public boolean access(String key, LimiterPolicy policy) {
        if (policy == null) {
            return true;
        }
        Long remain = redisTemplate.execute(redisScript, Lists.asList(key, new String[]{}), policy.toParams());
        log.info("限流器类别:{} | key :{} 限流器内许可数量为:{} ", policy.getClass().getSimpleName(), key, remain);
        return remain > 0;
    }

    @Override
    public boolean access(String key, String... args) {
        Long remain = redisTemplate.execute(redisScript, Lists.asList(key, new String[]{}), (Object) args);
        return remain > 0;
    }

}
