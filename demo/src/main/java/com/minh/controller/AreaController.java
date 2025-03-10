package com.minh.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;



@RestController
@RequestMapping("/api/v1")
public class AreaController {
    private static final String REDIS_URI = "redis://localhost:6379";


    @PostMapping("/setJson")
    public String fetchData() throws IOException {
        String geoJsonFile = "C:/Users/rongk/Downloads/custom.geo.json";
        RedisClient redisClient = RedisClient.create(REDIS_URI);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new File(geoJsonFile));
        String jsonString = objectMapper.writeValueAsString(root);
        StatefulRedisConnection<String, String> connection =   redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        syncCommands.set("geo_json_data", jsonString);

        return "Save successfully";
    }

    @GetMapping("/getJson")
    public String getData(@RequestParam String searchName) throws JsonProcessingException {
        RedisClient redisClient = RedisClient.create(REDIS_URI);

        StatefulRedisConnection<String, String> connection =   redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        ObjectMapper objectMapper = new ObjectMapper();

        String geoJsonString = syncCommands.get("geo_json_data");
        JsonNode geoJson = objectMapper.readTree(geoJsonString);

        String coodinates = "";
        String place = "";
        for (JsonNode feature : geoJson.get("features")) {
            String name = feature.get("properties").get("name").asText().toLowerCase();

            if (name.contains(searchName)) {
                JsonNode coordinates = feature.get("geometry").get("coordinates");
                place = name;
                coodinates = coordinates.toPrettyString();
                break;
            }
        }

        return  place +" - "+  coodinates;
    }

}
