local key = KEYS[1];
local value = ARGV[1];
local ttlMillis = tonumber(ARGV[2]);

local success = redis.call('SETNX', key, value);
if (success and success > 0) then
    redis.call('PEXPIRE', key, ttlMillis);
    return true;
else
    return false;
end
