--[[
   1. key : 通过这个 key 找到限流器stand-alone
   2. interval : 限流区间
   3. currentTime : 当前时间
   4. limit : 单位时间内的限流上限
]] --

-- 分布式限流器的 key
local key = KEYS[1]
-- 限流器的间隔
local interval = tonumber(ARGV[1])
-- 当前时间
local currentTime = tonumber(ARGV[2])
-- 限流器上线
local limit = tonumber(ARGV[3])

-- 拿到限流器
local bucket = redis.call('hgetall', key)

-- 当前区间内的令牌数
local currentTokens

-- 限流器不存在就初始化
if table.maxn(bucket) == 0 then

    -- 设置当前的许可数
    currentTokens = limit + 1
    -- 设置当前时间
    redis.call('hset', key, 'startTime', currentTime)

    -- 设置过期时间
    redis.call('pexpire', key, interval * 1.5)

-- 如果限流器存在
elseif table.maxn(bucket) == 4 then

    -- 上次使用时间
    local startTime = tonumber(bucket[2])

    -- 拿到当前区间内的令牌数
    currentTokens = tonumber(bucket[4])

    -- 判断是否到了下一个计数区间
    if currentTime - startTime >= interval then
        -- 到了就重设区间
        currentTokens = limit + 1
        redis.call('hset', key, 'startTime', currentTime)
    end
end

-- 使用计数器
currentTokens = currentTokens - 1

-- 返回当前可使用的令牌
if currentTokens < 0 then
    currentTokens = 0
else
    redis.call('hset', key, 'currentTokens', currentTokens)
end
return currentTokens