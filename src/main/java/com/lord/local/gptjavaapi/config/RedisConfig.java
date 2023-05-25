package com.lord.local.gptjavaapi.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RedisConfig {

    @Value("${lettuc.spring.redis.host}")
    private String redisHost;
    @Value("${lettuc.spring.redis.port}")
    private Integer redisPort;
    @Value("${lettuc.spring.redis.password}")
    private String redisPassword;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);
        redisConfig.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisConfig);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 可以进行其他配置，如序列化器等
        FastJsonRedisSerializer<Object> valueSerializer = new FastJsonRedisSerializer<>(Object.class);
        FastJsonRedisSerializer<String> keySerializer = new FastJsonRedisSerializer<>(String.class);

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        valueSerializer.setFastJsonConfig(fastJsonConfig);
        keySerializer.setFastJsonConfig(fastJsonConfig);
//        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        return redisTemplate;
    }
    // 其他配置...

}
