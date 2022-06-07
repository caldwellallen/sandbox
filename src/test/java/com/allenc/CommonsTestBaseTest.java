package com.allenc;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

import java.util.Arrays;

import static com.allenc.util.PropertyManager.loadProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Copyright 2022
 */
@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {8080})
class CommonsTestBaseTest extends TestBase {

    private static final String HOST = "localhost";
    private static final String ERROR = "Method Not Allowed";
    private static final String ERROR_PATH = "/service/v1/analyze";
    private static final String ERROR_MESSAGE = "This is an error message.";
    private static final int PORT = 8080;

    @BeforeEach
    void methodLevelSetup() {
        loadProperties("test.properties");
    }


    @Test
    void canRunCommand() {
        String result = runCommand(Arrays.asList("echo", "blah"));
        assertThat(result).isEqualTo("blah\n");
    }

    @Test
    void runCommandThrowsError() {
        Throwable thrown = catchThrowable(() -> runCommand(Arrays.asList("blah", "huh")));
        AssertionsForClassTypes.assertThat(thrown).isInstanceOf(AssertionError.class);
    }

    @Test
    void canRunRemoteCommand() {
        String result = runRemoteCommand("host", "username", "command");
        assertThat(result).contains("ssh: Could not resolve hostname host");
    }


}
