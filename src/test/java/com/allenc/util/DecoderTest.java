package com.allenc.util;

import static org.assertj.core.api.Assertions.assertThat;

import static com.allenc.util.Decoder.decodeEncodedMessage;

import com.allenc.TestBase;
import org.junit.jupiter.api.Test;

/**
 * @author Copyright 2022
 */
class DecoderTest extends TestBase {

    @Test
    void testDecoder() {
        String encodedMsg = "eJwNxMkNwDAIBMBW3BoW4lhI/LDY+pN5DAJ1NdwKei0KAe4uwdul9rDrwvRwQ3I0uETxB+dJX8L04zI+SVGLxEa1fNDSIlU=";
        String expected = "jhjksdhgfkjdsfhkjhjvblkajnlkdfmvlksjfdovbjaiudhv adjv ajgvoig avigogauguivadfhijbjkla";
        assertThat(decodeEncodedMessage(encodedMsg)).isEqualTo(expected);
    }
}
