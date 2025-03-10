package com.minh.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "kira4321";
    private static final long EXPIRATION_TIME = 3600000;

    public static String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.replaceFirst("Bearer ", "")
                : null;
    }

    public static String generateJwtToken(String token) {
        return JWT.create()
                .withSubject(token)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static String decodeJwtToken(String jwtToken) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(jwtToken);

            return decodedJWT.getSubject();
        } catch (Exception e) {
            return "Đăng nhập thất bại";
        }
    }
}
