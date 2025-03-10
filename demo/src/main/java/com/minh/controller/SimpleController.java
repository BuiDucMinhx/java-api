package com.minh.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import static com.minh.util.CommonUtil.*;


@RestController
@RequestMapping("/api/v1")
public class SimpleController {

    private String message = "Hello, World!";

    @GetMapping("/message")
    public String getMessage(HttpServletRequest httpServletRequest) {
        String token = getBearerToken(httpServletRequest);
    //    String generatedToken = generateJwtToken( token);

        return decodeJwtToken(token);
    }

    @PostMapping("/message")
    public String genToken(@RequestBody String newMessage) {

        return generateJwtToken( newMessage);
    }

    @DeleteMapping("/message")
    public String deleteMessage() {
        message = "Hello, World!";
        return "Message reset!";
    }
}
