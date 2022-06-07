package com.allenc.util;

import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Copyright 2022
 */
public class RandomGenerator {

    private RandomGenerator() {
        // Purely a helper class.
    }

    public static String randomAlphanumeric(int desiredLength) {
        return RandomStringUtils.randomAlphanumeric(desiredLength);
    }

    public static String randomHexValue(int desiredLength) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < desiredLength) {
            sb.append(Integer.toHexString(new Random().nextInt()));
        }
        return sb.toString().substring(0, desiredLength);
    }

    public static String randomNumeric(int desiredLength) {
        String intToReturn = RandomStringUtils.randomNumeric(desiredLength);
        while (intToReturn.startsWith("0")) {
            intToReturn = RandomStringUtils.randomNumeric(desiredLength);
        }
        return intToReturn;
    }

    public static String randomXRequestId() {
        return randomAlphanumeric(3).toUpperCase()
            + "-"
            + randomAlphanumeric(5).toUpperCase()
            + "-"
            + randomAlphanumeric(5).toUpperCase()
            + "-"
            + randomAlphanumeric(5).toUpperCase()
            + "-"
            + randomAlphanumeric(5).toUpperCase()
            + "-"
            + randomAlphanumeric(3).toUpperCase();
    }

    public static String randomUserId() {
        return randomNumeric(3).toUpperCase()
            + "-"
            + randomNumeric(3).toUpperCase()
            + "-"
            + randomNumeric(3).toUpperCase()
            + "-"
            + randomNumeric(5).toUpperCase();
    }
}
