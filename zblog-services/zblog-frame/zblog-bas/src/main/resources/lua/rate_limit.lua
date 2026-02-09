-- 限流 Key
local key = KEYS[1]

-- 当前时间（毫秒）
local now = tonumber(ARGV[1])

-- 时间窗口（毫秒）
local window = tonumber(ARGV[2])

-- 最大请求次数
local maxCount = tonumber(ARGV[3])

-- 请求唯一标识
local member = ARGV[4]

-- 1. 删除窗口外的请求记录
redis.call(
    "ZREMRANGEBYSCORE",
    key,
    0,
    now - window
)

-- 2. 获取窗口内请求数量
local count = redis.call("ZCARD", key)

-- 3. 判断是否超限
if count >= maxCount then
    return 0
end

-- 4. 记录本次请求
redis.call("ZADD", key, now, member)

-- 5. 使用毫秒级过期时间
-- 每次访问都会刷新 TTL，防止 key 提前过期
redis.call("PEXPIRE", key, window)

return 1
