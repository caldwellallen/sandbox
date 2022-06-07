package com.allenc.util;


import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static com.allenc.CommonsTestConstants.SECRET_KEY;

import com.allenc.TestBase;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;


/**
 * @author Copyright 2022
 */
class CryptoUtilsTest extends TestBase {

    private static final String ORIGINAL = "notSoSecretString";
    private static final String DECRYPTED = "5fZQI14I/2GQsNjJRwp1TcmgEukCE9J+jCqe1ysITuQcR1LvgRHeTQtxEQyN";
    private static final SecretKey BAD_SECRET_KEY = new SecretKeySpec("ssshhhhhhhhhhhhh".getBytes(), "AESNOT");

    @Test
    void canEncryptStringChangesEveryTime() {
        String encrypted = CryptoUtils.encrypt(ORIGINAL, SECRET_KEY);
        assertThat(CryptoUtils.decrypt(encrypted, SECRET_KEY)).isNotNull();
        assertThat(CryptoUtils.decrypt(encrypted, SECRET_KEY)).isNotEqualTo(DECRYPTED);
    }

    @Test
    void canDecryptPreviouslyEncryptedString() {
        assertThat(CryptoUtils.decrypt(DECRYPTED, SECRET_KEY)).isEqualTo(ORIGINAL);
    }

    @Test
    void encryptThrowsIllegalStateException() {
        Throwable thrown = catchThrowable(() -> CryptoUtils.encrypt(ORIGINAL, BAD_SECRET_KEY));
        assertThat(thrown).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void decryptThrowsIllegalArgumentException() {
        Throwable thrown = catchThrowable(() -> CryptoUtils.decrypt(ORIGINAL, BAD_SECRET_KEY));
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

}
