package com.minh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/v1")
public class RedisController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/set-redis")
    public String setCache(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value, 50000, TimeUnit.DAYS);
        return "Saved successfully!";
    }

    @GetMapping("/get-redis")
    public String getCache(@RequestParam String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
