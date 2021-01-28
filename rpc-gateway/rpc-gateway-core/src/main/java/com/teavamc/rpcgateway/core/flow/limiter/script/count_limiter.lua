--[[
   1. key : 通过这个 key 找到限流器
   2. interval : 限流区间
   3. currentTime : 当前时间
   4. limit : 单位时间内的限流上限
]] --

local key = KEYS[1]
local interval = tonumber(ARGV[1])
local currentTime = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])
local bucket = redis.call('hgetall', key)

local currentTokens

if table.maxn(bucket) == 0 then
    -- first check if bucket not exists, if yes, create a new one , then grant access
    currentTokens = limit + 1
    redis.call('hset', key, 'startTime', currentTime)
    -- set expire time when init the key
    redis.call('pexpire', key, interval * 1.5)
elseif table.maxn(bucket) == 4 then
    local startTime = tonumber(bucket[2])
    currentTokens = tonumber(bucket[4])
    if currentTime - startTime >= interval then
        -- reset limit
        currentTokens = limit + 1
        redis.call('hset', key, 'startTime', currentTime)
    end
end

currentTokens = currentTokens - 1
if currentTokens < 0 then
    currentTokens = 0
else
    redis.call('hset', key, 'currentTokens', currentTokens)
end
return currentTokens