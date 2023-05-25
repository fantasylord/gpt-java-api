package com.lord.local.gptjavaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DistributedLockService {

    private final RedisTemplate<String, String> redisTemplate;
//    private final boolean renewEnabled;

    @Autowired
    public DistributedLockService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
//        this.renewEnabled = renewEnabled;
    }

    public boolean acquireLock(String lockKey, long expireTime) {
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked");
        if (lockAcquired != null && lockAcquired) {
            redisTemplate.expire(lockKey, expireTime, TimeUnit.MILLISECONDS);
            return true;
        } else if (false) {
            // 锁已经存在，检查是否需要续命
            Long remainingTime = redisTemplate.getExpire(lockKey, TimeUnit.MILLISECONDS);
            if (remainingTime != null && remainingTime < expireTime) {
                redisTemplate.expire(lockKey, expireTime, TimeUnit.MILLISECONDS);
            }
        }
        return false;
    }

    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}
