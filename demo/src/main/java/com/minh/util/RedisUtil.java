package com.minh.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Component
public class RedisUtil {
    private static final String REDIS_URI = "redis://localhost:6379";

    public void setDataRegion(String fileUrl, String key) throws IOException {
        RedisClient redisClient = RedisClient.create(REDIS_URI);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new File(fileUrl));
        String jsonString = objectMapper.writeValueAsString(root);
        StatefulRedisConnection<String, String> connection =   redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        syncCommands.set(key, jsonString);
        syncCommands.set(key, jsonString);
    }
}
