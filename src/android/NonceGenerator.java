package com.example.utils;
import java.security.SecureRandom;
import java.util.Base64;

public class NonceGenerator {

    public static String generateNonce(int size) {
        // Size in bytes
        byte[] nonce = new byte[size];
        // SecureRandom is a strong random number generator
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(nonce);
        // Encode to Base64 to make it URL-safe and return as a String
        return Base64.getUrlEncoder().withoutPadding().encodeToString(nonce);
    }
}
