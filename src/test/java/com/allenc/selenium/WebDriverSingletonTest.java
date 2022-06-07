package com.allenc.selenium;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.SAFARI;

import static com.allenc.selenium.Browser.CHROMIUM;
import static com.allenc.util.PropertyManager.clearProperties;
import static com.allenc.util.PropertyManager.getProperty;
import static com.allenc.util.PropertyManager.loadProperty;

import com.allenc.TestBase;
import com.allenc.CommonsTestBase;

import java.util.Objects;

import com.allenc.selenium.page.google.GoogleSearchPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * @author Copyright 2022
 */
class WebDriverSingletonTest extends TestBase {

    @Test
    void canQuitDriver() {
        runTestsAsSelenium();
        assertThat(getDriver()).isNotNull();
        WebDriverSingleton.cleanupDriver();
        Throwable thrown = catchThrowable(() -> getDriver().get("http://www.google.com/"));
        assertThat(thrown).isInstanceOf(org.openqa.selenium.NoSuchSessionException.class);
        runTestsAsSelenium();
        getDriver().get("http://www.google.com/");
    }

    @ParameterizedTest
    @EnumSource(value = Browser.class, names = {"FIREFOX", "CHROME", "HEADLESS_FIREFOX", "HEADLESS_CHROME"})
    void canRunTheseBrowsersBothOnGridAndLocally(Browser browser) {
        loadProperty("browser", browser.name());
        runTestsAsSelenium();
        getDriver().get("https://www.google.com/");
        GoogleSearchPage homePage = new GoogleSearchPage(getDriver());
        assertThat(homePage.getDriver().toString()).containsIgnoringCase(browser.name().contains("HEADLESS_") ? browser.name().split("_")[1] : browser.name());
    }

    @Test
    void unknownBrowserFails() {
        loadProperty("browser", "UNKNOWN");
        Throwable thrown = catchThrowable(CommonsTestBase::runTestsAsSelenium);
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullBrowserFails() {
        clearProperties();
        Throwable thrown = catchThrowable(CommonsTestBase::runTestsAsSelenium);
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

}
