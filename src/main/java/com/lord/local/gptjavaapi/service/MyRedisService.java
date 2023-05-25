package com.lord.local.gptjavaapi.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MyRedisService {

    private final RedisTemplate<String, String> redisTemplateForString;
    private final RedisTemplate<String, Object> redisTemplateForObj;

    @Autowired
    public MyRedisService(RedisTemplate<String, String> redisTemplate,RedisTemplate<String,Object> redisTemplateForObj) {
        this.redisTemplateForString = redisTemplate;
        this.redisTemplateForObj = redisTemplateForObj;
    }

    public boolean readAndUpdateValue(String key, String newValue, long expirationSeconds) {
        // 读取值
        String value = redisTemplateForString.opsForValue().get(key);
        System.out.println("Current value: " + value);
        if(value==null){
            return false;
        }
        Boolean executeResult = redisTemplateForString.execute((RedisCallback<Boolean>) connection -> {
            try {
                // 开启事务
                connection.multi();
                // 更新值
                redisTemplateForString.opsForValue().set(key, newValue);
                // 设置过期时间
                redisTemplateForString.expire(key, expirationSeconds, TimeUnit.SECONDS);
                // 提交事务
                connection.exec();
                return true;
            } catch (Exception e) {
                log.error("redis 事务执行失败,e:{},", e.getMessage(), e);
                return false;
            }
        });
        return executeResult;
    }

    public String readValueForStr(String key) {
        // 读取值
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String value = redisTemplateForString.opsForValue().get(key);
        return value;
    }
    public Object readValueForObject(String key){
        // 读取值
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Object value = redisTemplateForObj.opsForValue().get(key);
        return value;
    }
    public  boolean putString(String key,String value,long expirationMilliseconds){
        try {
            redisTemplateForString.opsForValue().set(key, value,expirationMilliseconds, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            log.error("redis error,e:{},", e.getMessage(), e);
            return false;
        }
    }
    public boolean putObject(String key,Object value, long expirationMilliseconds){
        Boolean executeResult = redisTemplateForString.execute((RedisCallback<Boolean>) connection -> {
            try {
                // 开启事务
//                connection.multi();
                // 更新值 &   设置过期时间
                redisTemplateForObj.opsForValue().set(key, value,expirationMilliseconds, TimeUnit.MILLISECONDS);
                // 设置过期时间
//                redisTemplateForObj.expire(key, expirationMilliseconds, TimeUnit.MILLISECONDS);
                // 提交事务
//                connection.exec();
                return true;
            } catch (Exception e) {
                log.error("redis 事务执行失败,e:{},", e.getMessage(), e);
                return false;
            }
        });
        return executeResult;
    }
    public boolean acquireLock(String lockKey, long expireTime) {
        Boolean lockAcquired = redisTemplateForString.opsForValue().setIfAbsent(lockKey, "locked");
        if (lockAcquired != null && lockAcquired) {
            redisTemplateForString.expire(lockKey, expireTime, TimeUnit.MILLISECONDS);
            return true;
        } else if (false) {
            // 锁已经存在，检查是否需要续命
            Long remainingTime = redisTemplateForString.getExpire(lockKey, TimeUnit.MILLISECONDS);
            if (remainingTime != null && remainingTime < expireTime) {
                redisTemplateForString.expire(lockKey, expireTime, TimeUnit.MILLISECONDS);
            }
        }
        return false;
    }

    public void releaseLock(String lockKey) {
        redisTemplateForString.delete(lockKey);
    }
}
