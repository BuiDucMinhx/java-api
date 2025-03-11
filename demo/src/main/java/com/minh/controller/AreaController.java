package com.minh.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minh.util.RedisUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;



@RestController
@RequestMapping("/api/v1")
public class AreaController {
    @Autowired
    private RedisUtil redisUtil;

    private static final String REDIS_URI = "redis://localhost:6379";
    private static final String GEO_JSON_COODINATES = "geo_json_data";
    private static final String GEO_JSON_REGION = "geo_json_region_data";



    @PostMapping("/setJson/{isRegion}")
    public String fetchData(@PathVariable boolean isRegion) throws IOException {
        String geoJsonFile = "C:/Users/rongk/Downloads/vn.json";
        String geoJsonRegion = "C:/Users/rongk/Downloads/full_json_generated_data_vn_units.json";

        if (isRegion) {
            redisUtil.setDataRegion(geoJsonFile, GEO_JSON_COODINATES);
        } else {
            redisUtil.setDataRegion(geoJsonRegion, GEO_JSON_REGION);
        }

        return "Save successfully";
    }

    @GetMapping("/getArea")
    public String getArea(@RequestParam String searchName,
                          @RequestParam String type) throws JsonProcessingException {
        RedisClient redisClient = RedisClient.create(REDIS_URI);

        StatefulRedisConnection<String, String> connection =   redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        ObjectMapper objectMapper = new ObjectMapper();
        String geoJsonString = syncCommands.get(GEO_JSON_REGION);
        JsonNode geoJson =  objectMapper.readTree(geoJsonString);

        for (JsonNode node : geoJson) {
            JsonNode districts = node.get("District");
            if (!searchName.isEmpty() && node.get("NameEn").toPrettyString().toLowerCase().contains(searchName)
                && type.equals(node.get("Type").toPrettyString().toLowerCase().replace("\"", ""))) {
                return objectMapper.writeValueAsString(node);
            }
            for (JsonNode district : districts) {
                JsonNode wards = district.get("Ward");
                if (!searchName.isEmpty() && district.get("NameEn").toPrettyString().toLowerCase().contains(searchName)
                    && type.equals(district.get("Type").toPrettyString().toLowerCase().replace("\"", ""))) {
                    return objectMapper.writeValueAsString(district);
                }

                for (JsonNode ward : wards) {
                    if (!searchName.isEmpty() && ward.get("NameEn").toPrettyString().toLowerCase().contains(searchName)
                        && type.equals(ward.get("Type").toPrettyString().toLowerCase().replace("\"", ""))) {
                        return objectMapper.writeValueAsString(ward);
                    }
                }

            }
        }

        return  geoJsonString;
    }

    @GetMapping("/getJson")
    public String getData(@RequestParam String searchName) throws JsonProcessingException {
        RedisClient redisClient = RedisClient.create(REDIS_URI);

        StatefulRedisConnection<String, String> connection =   redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        ObjectMapper objectMapper = new ObjectMapper();

        String geoJsonString = syncCommands.get(GEO_JSON_COODINATES);
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
