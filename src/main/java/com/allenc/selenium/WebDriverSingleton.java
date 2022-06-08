package com.allenc.selenium;

import com.allenc.util.PropertyManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static org.junit.Assert.fail;

/**
 * @author Copyright 2022
 */
@Log4j2
public class WebDriverSingleton {

    private static WebDriver driver;
    private static SessionId sessionId;
    private static URL remoteServerAddress;

    private WebDriverSingleton() {
        // private
    }

    public static WebDriver getInstance() {
        if (!isDriverRunning()) {
            boolean useSeleniumGrid = Boolean.parseBoolean(PropertyManager.getProperty("selenium.use.seleniumGrid"));
            Browser browser = Browser.valueOf(Objects.requireNonNull(PropertyManager.getProperty("browser")).toUpperCase());
            log.debug("Testing browser: {}", browser);
            driver = getDriver(browser, useSeleniumGrid);
        }
        return driver;
    }

    public static void cleanupDriver() {
        if (driver != null) {
            try {
                driver.quit();
                driver = null;
            } catch (NoSuchSessionException e) {
                log.debug("NoSuchSessionException thrown, should not be a problem");
            }
        }
    }

    public static boolean isDriverRunning() {
        return driver != null;
    }

    private static WebDriver getDriver(Browser browser, boolean useSeleniumGrid) {
        String pathToDrivers = "path.to.drivers";
        WebDriver driver;
        boolean isHeadless = browser.name().startsWith("HEADLESS");
        switch (browser) {
            case FIREFOX:
            case HEADLESS_FIREFOX:
                System.setProperty("webdriver.gecko.driver", PropertyManager.getProperty(pathToDrivers) + "geckodriver");
                driver = createDriver(browser, useSeleniumGrid, isHeadless);
                break;
            case CHROME:
            case HEADLESS_CHROME:
            case CHROMIUM:
                System.setProperty("webdriver.chrome.driver", PropertyManager.getProperty(pathToDrivers) + "chromedriver");
                driver = createDriver(browser, useSeleniumGrid, isHeadless);
                break;
            case EDGE:
                System.setProperty("webdriver.edge.driver", PropertyManager.getProperty(pathToDrivers) + "msedgedriver");
                driver = createDriver(browser, useSeleniumGrid, isHeadless);
                break;
            case OPERA:
                System.setProperty("webdriver.opera.driver", PropertyManager.getProperty(pathToDrivers) + "operadriver");
                driver = createDriver(browser, useSeleniumGrid, isHeadless);
                break;
            case SAFARI:
                driver = createDriver(browser, useSeleniumGrid, isHeadless);
                break;
            default:
                throw new WebDriverException();
        }
        return driver;
    }

    private static WebDriver createFirefoxDriver(boolean isHeadless) {
        FirefoxOptions options = new FirefoxOptions()
                .setAcceptInsecureCerts(true)
                .setHeadless(isHeadless);
        return new FirefoxDriver(options);
    }

    private static ChromeDriver createChromeDriver(boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();
        if (isHeadless) {
            options.addArguments("--headless");
        }
        options.addArguments("--no-sandbox");
        return new ChromeDriver();
    }

    private static ChromeDriver createChromiumDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/usr/bin/chromium-browser");
        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    private static OperaDriver createOperaDriver() {
        OperaOptions options = new OperaOptions();
        options.setBinary("/usr/bin/opera");
        return new OperaDriver(options);
    }

    private static EdgeDriver createEdgeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.setBinary("/usr/bin/microsoft-edge-dev");
        return new EdgeDriver((Capabilities) chromeOptions);
    }

    private static SafariDriver createSafariDriver() {
        //todo: mac user needs to add code here
        return new SafariDriver(new SafariOptions());
    }

    private static URL getSeleniumHubUrl() {
        URL seleniumHubURL = null;
        try {
            seleniumHubURL = new URL(System.getProperty("se3hub", PropertyManager.getProperty("se.hub.url")));
        } catch (MalformedURLException e) {
            fail("MalformedURLException thrown");
        }
        return seleniumHubURL;
    }

    private static WebDriver createDriver(Browser browser, boolean useSeleniumGrid, boolean isHeadless) {
        switch (browser) {
        case FIREFOX:
        case HEADLESS_FIREFOX:
            return createFirefoxDriver(isHeadless);
            case CHROME:
            case HEADLESS_CHROME:
                return createChromeDriver(isHeadless);
            case EDGE:
                return createEdgeDriver();
            case OPERA:
                return createOperaDriver();
            case CHROMIUM:
                return createChromiumDriver();
            case SAFARI:
                return createSafariDriver();
            default:
                throw new WebDriverException();
        }
    }

    public static SessionId getSessionId() {
        return sessionId;
    }

    public static URL getRemoteServerAddress() {
        return remoteServerAddress;
    }

}
