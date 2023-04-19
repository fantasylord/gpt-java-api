package com.lord.local.gptjavaapi.uitls;

import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class ScryptUtil {
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 32;
    private static final int ITERATIONS = 32768;
    private static final int MEMORY_COST = 8;

    public static String hashPassword(String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return MEMORY_COST + ":" + ITERATIONS + ":" + SALT_LENGTH + ":" + toHex(salt) + ":" + toHex(hash);
    }

    public static void main(String[] args) {
        String helloWord = null;
        try {
            helloWord = hashPassword("helloWord");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(helloWord);
    }
    public static boolean verifyPassword(String password, String hash) throws Exception {
        String[] parts = hash.split(":");
        int memoryCost = Integer.parseInt(parts[0]);
        int iterations = Integer.parseInt(parts[1]);
        int saltLength = Integer.parseInt(parts[2]);
        byte[] salt = fromHex(parts[3]);
        byte[] storedHash = fromHex(parts[4]);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashToVerify = factory.generateSecret(spec).getEncoded();

        return slowEquals(storedHash, hashToVerify);
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
