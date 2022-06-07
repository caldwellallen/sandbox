package com.allenc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;

/**
 * @author Copyright 2022
 */
@Log4j2
public class Decoder {

    private Decoder() {
        // purely a static class
    }

    public static String decodeEncodedMessage(String encodedMessage) {
        byte[] bytes = Base64.decodeBase64(encodedMessage);
        String message = new String(decompress(bytes));
        log.debug("Encoded message decoded to: {}", message);
        return message;
    }

    ////////// private methods...
    private static byte[] decompress(byte[] data) {
        Inflater inflater = new Inflater();
        try (ByteArrayOutputStream inflaterOutputStream = new ByteArrayOutputStream()) {
            inflater.setInput(data);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                inflaterOutputStream.write(buffer, 0, count);
            }
            return inflaterOutputStream.toByteArray();
        } catch (IOException | DataFormatException ex) {
            return new byte[0];
        } finally {
            // frees native (off-heap) memory
            inflater.end();
        }
    }
}
