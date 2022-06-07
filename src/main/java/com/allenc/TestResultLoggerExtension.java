package com.allenc;

import com.allenc.selenium.BasePage;
import com.allenc.selenium.WebDriverSingleton;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.time.Duration;

import static com.allenc.CommonsTestBase.getNowToMillis;
import static com.allenc.CommonsTestBase.getStartTestTime;

/**
 * @author
 */
@Slf4j
public class TestResultLoggerExtension implements TestWatcher, BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        log.info("STARTING: {}", getTestName(context));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logStatus(context, TestResultStatus.SUCCESS);
        cleanupDriverIfNecessary();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable throwable) {
        logStatus(context, TestResultStatus.FAILURE);
        BasePage.takeScreenShot();
        cleanupDriverIfNecessary();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable throwable) {
        logStatus(context, TestResultStatus.ABORTED);
        cleanupDriverIfNecessary();
    }

    ////////// private methods...
    private void logStatus(ExtensionContext context, TestResultStatus status) {
        log.info("FINISHED:{} -> {} ({})", getTestName(context), status, getStartTestTime() != null ? getRunTime() : "UNKNOWN");
    }

    private String getRunTime() {
        return Duration.between(getStartTestTime(), getNowToMillis())
                .toString()
                .replace("PT", "")
                .replace("S", " Seconds");
    }

    private void cleanupDriverIfNecessary() {
        if (WebDriverSingleton.isDriverRunning()) {
            WebDriverSingleton.cleanupDriver();
        }
    }

    private String getTestName(ExtensionContext context) {
        return context.getTestMethod().stream()
                .findFirst()  // returns Optional
                .map(Object::toString)
                .orElse("").replace("void ", "");
    }

    private enum TestResultStatus {
        SUCCESS,
        ABORTED,
        FAILURE
    }
}
