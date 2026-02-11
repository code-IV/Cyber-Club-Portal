package com.cyberclub.identity.user;

import com.cyberclub.identity.config.JwtProperties;
import com.cyberclub.identity.services.JwtTokenService;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class TestJwtFactory {

    private static final String SECRET =
            "test-secret-key-test-secret-key-test-secret-key"; 
    // Must be at least 32 bytes for HS256

    public static JwtTokenService createTokenService() {
        JwtProperties properties = new JwtProperties(SECRET, 900L);
        return new JwtTokenService(properties);
    }

    public static SecretKey key() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }
}
