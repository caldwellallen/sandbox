package com.allenc.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import lombok.extern.log4j.Log4j2;

/**
 * @author Copyright 2022
 */
@Log4j2
public class CryptoUtils {

    private static final String EXCEPTION_MSG = "Exception was thrown: {}";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    private CryptoUtils() {
        //purely a static class
    }

    public static String encrypt(String privateString, SecretKey skey) {
        byte[] iv = new byte[GCM_IV_LENGTH];
        (new SecureRandom()).nextBytes(iv);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.debug(EXCEPTION_MSG, e.getMessage());
        }
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        try {
            assert cipher != null;
            cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.debug(EXCEPTION_MSG, e.getMessage());
        }

        byte[] ciphertext = new byte[0];
        try {
            ciphertext = cipher.doFinal(privateString.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.debug(EXCEPTION_MSG, e.getMessage());
        }
        byte[] encrypted = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encrypted, SecretKey skey) {
        byte[] decoded = Base64.getDecoder().decode(encrypted);

        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.debug(EXCEPTION_MSG, e.getMessage());
        }
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        try {
            assert cipher != null;
            cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.debug(EXCEPTION_MSG, e.getMessage());
        }

        byte[] ciphertext = new byte[0];
        try {
            ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.debug(EXCEPTION_MSG, e.getMessage());
        }

        return new String(ciphertext, StandardCharsets.UTF_8);
    }

}

