package com.spring.interview.cache.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

/**
 * Production Redis distributed lock using SETNX with TTL and atomic Lua script for release.
 */
@Component
public class RedisAtomicDistributedLock {

    private final StringRedisTemplate redisTemplate;

    public static final String UNLOCK_LUA_SCRIPT = """
        if redis.call('get', KEYS[1]) == ARGV[1] then
            return redis.call('del', KEYS[1])
        else
            return 0
        end
    """;

    private final DefaultRedisScript<Long> unlockScript;

    public RedisAtomicDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.unlockScript = new DefaultRedisScript<>(UNLOCK_LUA_SCRIPT, Long.class);
    }

    public String acquireLock(String lockKey, Duration ttl) {
        String token = UUID.randomUUID().toString();
        Boolean acquired = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, token, ttl);

        return Boolean.TRUE.equals(acquired) ? token : null;
    }

    public boolean releaseLock(String lockKey, String token) {
        if (token == null) return false;
        Long result = redisTemplate.execute(unlockScript, Collections.singletonList(lockKey), token);
        return result != null && result == 1L;
    }
}
