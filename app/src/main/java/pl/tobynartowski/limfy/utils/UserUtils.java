package pl.tobynartowski.limfy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserUtils {

    private static UserUtils instance;
    private SharedPreferences preferences;

    private UserUtils(Context context) {
        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public static UserUtils getInstance(Context context) {
        if (instance == null) {
            instance = new UserUtils(context);
        }
        return instance;
    }

    public void setSession(String id, String bearer) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", id);
        editor.putString("bearer", bearer);
        editor.apply();
    }

    public String getId() {
        return preferences.getString("id", null);
    }

    public String getBearer() {
        return preferences.getString("bearer", null);
    }

    public void destroySession() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

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
