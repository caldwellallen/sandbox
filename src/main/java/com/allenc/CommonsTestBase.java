package com.allenc;

import com.allenc.selenium.WebDriverSingleton;
import com.allenc.util.PropertyManager;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;

/**
 * @author Copyright 2022
 */
@ExtendWith(TestResultLoggerExtension.class)
@Log4j2
public class CommonsTestBase {

    private static Instant startTestTime;
    private static WebDriver driver;

    public static void setRestAssuredTo(String host, String port, String basePath) {
        RestAssured.reset();
        if (!Strings.isNullOrEmpty(port)) {
            RestAssured.port = Integer.parseInt(port);
        }
        RestAssured.baseURI = host;
        RestAssured.basePath = basePath;
    }

    public static Instant getStartTestTime() {
        return startTestTime;
    }

    public static void setStartTestTime() {
        startTestTime = getNowToMillis();
    }

    public static Instant getNowToMillis() {
        return Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    public static void sleepFor(long howLong) {
        try {
            Thread.sleep(howLong);
        } catch (InterruptedException e) {
            log.debug("sleepFor threw exception");
            Thread.currentThread().interrupt();
        }
    }

    protected static void runTestsAsSelenium() {
        driver = WebDriverSingleton.getInstance();
    }

    @BeforeEach
    void testCommonsBaseSetup() {
        RestAssured.reset();
        setStartTestTime();
    }

    protected String runCommand(List<String> commands) {
        try {
            String command = String.join(" ", commands);
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
            int exitValue = process.exitValue();
            String console = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            process.destroy();
            if (exitValue != 0) {
                log.debug("Error Calling {} had exit code {}", command, exitValue);
            }
            return console;
        } catch (Exception e) {
            log.error("Error running command", e);
            fail("Error running command", e);
            return null;
        }
    }

    protected String runRemoteCommand(String host, String userName, String command) {
        log.info("Running command: \"{}\" on: {}", command, PropertyManager.getProperty("deployHostName"));
        return runCommand(Arrays.asList("ssh", userName + "@" + host, command));
    }

    protected WebDriver getDriver() {
        return driver;
    }

}
