package pl.tobynartowski.limfy.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserUtils {

    public static String hashPassword(String plainText) {
        String hash = plainText;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] digest = messageDigest.digest();
            hash = String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }
}
