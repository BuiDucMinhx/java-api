package com.minh.util;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {

    public static String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Cắt "Bearer " để lấy token
        }
        return null; // Trả về null nếu không có token hợp lệ
    }
}
