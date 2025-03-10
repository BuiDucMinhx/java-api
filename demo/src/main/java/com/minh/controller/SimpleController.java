package com.minh.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class SimpleController {

    private String message = "Hello, World!";

    @GetMapping("/message")
    public String getMessage(HttpServletRequest request) {
        // Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract token after "Bearer "
            String token = authHeader.substring(7);
            System.out.println("Token: " + token);
            return token;
        } else {
            return "No Bearer token found";
        }




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
