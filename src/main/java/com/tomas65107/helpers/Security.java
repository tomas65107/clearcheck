package com.tomas65107.helpers;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Security {
    private static final String SERVER_SECRET = "ClearCheckV79GXDNEp9Pf8qUK4cé6H9qNHEj5M8vuYôcHcsHXK§AZend";

    public static String hmac(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(key);
            byte[] result = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String data, String key) {
        try {
            String safeKey = String.format("%-16s", key).substring(0, 16);
            SecretKeySpec secret = new SecretKeySpec(safeKey.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(data.getBytes())
            );

        } catch (Exception e) {throw new RuntimeException(e);}
    }

    public static String decrypt(String encrypted, String key) {
        try {
            String safeKey = String.format("%-16s", key).substring(0, 16);
            SecretKeySpec secret = new SecretKeySpec(safeKey.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));

        } catch (Exception e) {
            return null;
        }
    }
}
