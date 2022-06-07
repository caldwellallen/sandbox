package com.allenc;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Copyright 2022
 */
public class CommonsTestConstants {

    public static final String DATE_FORMAT_TO_SECONDS = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_FORMAT_TO_MILLI = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]'Z'";
    public static final String DATE_FORMAT_TO_MICRO = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]'Z'";
    public static final String SPRING_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]XXX";
    public static final String FOUR_BYTE_CHARS = "\uD83D\uDE0D";
    public static final String THREE_BYTE_CHARS = "朋友 বন্ধু";

    public static final long ACCEPTABLE_TIME_DIFF = 10000;
    public static final long A_QUARTER_SECOND = 250;
    public static final long A_HALF_SECOND = 500;
    public static final long A_SECOND = 1000;

    public static final SecretKey SECRET_KEY = new SecretKeySpec("ssshhhhhhhhhhhhh".getBytes(), "AES");

    private CommonsTestConstants() {
        // purely a static class
    }
}
