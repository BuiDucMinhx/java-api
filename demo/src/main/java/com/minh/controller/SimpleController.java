package com.minh.controller;


import com.minh.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class SimpleController {

    private String message = "Hello, World!";

    @GetMapping("/message")
    public String getMessage(HttpServletRequest request) {
        CommonUtil.getBearerToken(request);


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
