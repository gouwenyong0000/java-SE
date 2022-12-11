
-- 根据传入的 key  和value 校验删除   返回值LONG
local key1 = KEYS[1]
local uid = KEYS[2]
if redis.call("get", key1) == uid then
    redis.call("del", uid)
    return 1
else
    return -1
end

--[[
hset  表示锁所有者 和重入次数
hset key filed value
key  == 锁名称
filed 服务uid + 线程id
value == 重入次数

]]
--[[
加锁
    1. 判断锁是否存在（`exists`），则直接获取锁 `hset key field value`
    2. 如果锁存在则判断是否自己的锁（`hexists`），如果是自己的锁则重入：`hincrby key field increment`
    3. 否则重试：递归 循环
]]
if (redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1)
then
    redis.call('hincrby', KEYS[1], ARGV[1], 1);
    redis.call('expire', KEYS[1], ARGV[2]);
    return 1;
else
	return 0;
end

--[[
解锁
    1. 判断自己的锁是否存在（hexists），不存在则返回nil
    2. 如果自己的锁存在，则减1（hincrby—1），判断减1后的值是否为0，为0则释放锁（del）并返回1
    3. 不为0，返回0
]]
if(redis.call('hexists', KEYS[1], ARGV[1]) == 0) then
    return nil;
elseif(redis.call('hincrby', KEYS[1], ARGV[1], -1) > 0) then
    return 0;
else
    redis.call('del', KEYS[1]);
    return 1;
end;