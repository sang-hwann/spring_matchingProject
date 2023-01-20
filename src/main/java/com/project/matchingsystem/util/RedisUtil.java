package com.project.matchingsystem.util;

import com.project.matchingsystem.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    
    private final RedisTemplate<String, String> redisTemplate;

    public void setRefreshToken(String key, String value) {
        redisTemplate.opsForValue().set("refresh" + key, value, JwtProvider.REFRESH_TOKEN_TIME, TimeUnit.MILLISECONDS);
    }

    public boolean isExistsRefreshToken(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("refresh" + key));
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get("refresh" + key);
    }

    public String getAccessToken(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken);
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete("refresh" + key);
    }

    public void setAccessTokenInBlackList(String accessToken, Long expiration) {
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

}
