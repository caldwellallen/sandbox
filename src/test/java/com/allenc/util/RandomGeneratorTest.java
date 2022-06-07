package com.allenc.util;

import static org.assertj.core.api.Assertions.assertThat;

import static com.allenc.util.RandomGenerator.randomAlphanumeric;
import static com.allenc.util.RandomGenerator.randomHexValue;
import static com.allenc.util.RandomGenerator.randomNumeric;
import static com.allenc.util.RandomGenerator.randomUserId;
import static com.allenc.util.RandomGenerator.randomXRequestId;

import com.allenc.TestBase;
import org.junit.jupiter.api.Test;

/**
 * @author Copyright 2022
 */
class RandomGeneratorTest extends TestBase {

    @Test
    void verifyRandomAlphanumeric() {
        String alphaNumbericNumber = randomAlphanumeric(22);
        assertThat(alphaNumbericNumber).hasSize(22).containsPattern("^[a-zA-Z0-9]*$");
    }

    @Test
    void verifyRandomHexValue() {
        String hexValue = randomHexValue(12);
        assertThat(hexValue).hasSize(12).containsPattern("^[0-9A-Fa-f]+$");
    }

    @Test
    void verifyRandomNumericValue() {
        String numericValue = randomNumeric(44);
        assertThat(numericValue).hasSize(44).containsPattern("^[0-9+$]");
    }

    @Test
    void verifyRandomXRequestId() {
        String xRequestId = randomXRequestId();
        assertThat(xRequestId).containsPattern("^[A-Z,0-9]{3}-[A-Z,0-9]{5}-[A-Z,0-9]{5}-[A-Z,0-9]{5}-[A-Z,0-9]{5}-[A-Z,0-9]{3}$");
    }

    @Test
    void verifyRandomUserId() {
        String userId = randomUserId();
        assertThat(userId).containsPattern("^[A-Z,0-9]{3}-[A-Z,0-9]{3}-[A-Z,0-9]{3}-[A-Z,0-9]{5}$");
    }

}
