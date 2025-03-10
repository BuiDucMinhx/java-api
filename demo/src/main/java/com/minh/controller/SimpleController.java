package com.minh.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import static com.minh.util.CommonUtil.getBearerToken;


@RestController
@RequestMapping("/api/v1")
public class SimpleController {

    private String message = "Hello, World!";

    @GetMapping("/message")
    public String getMessage(HttpServletRequest httpServletRequest) {
        String token = getBearerToken(httpServletRequest);
        return message;
    }

    @PostMapping("/message")
    public String updateMessage(@RequestBody String newMessage) {
        message = newMessage;
        return "Message updated!";
    }

    @DeleteMapping("/message")
    public String deleteMessage() {
        message = "Hello, World!";
        return "Message reset!";
    }
}
