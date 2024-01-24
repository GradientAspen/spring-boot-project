package com.project.springbootproject.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {
    private static final String CRYPTO_ALG = "SHA-512";
    private static final int DEFAULT_CAPACITY = 16;
    private static final String FORMAT_SPECIFIER = "%02x";

    public HashUtil() {
    }

    public static byte[] getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[DEFAULT_CAPACITY];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) {
        StringBuilder hashPassword = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(CRYPTO_ALG);
            messageDigest.update(salt);
            byte[] digest = messageDigest.digest(password.getBytes());
            for (byte b : digest) {
                hashPassword.append(String.format(FORMAT_SPECIFIER, b));
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not creat hash ", e);
        }
        return hashPassword.toString();
    }
}
