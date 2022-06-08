package com.allenc.selenium;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import javax.imageio.ImageIO;

import com.allenc.CommonsTestBase;
import com.allenc.CommonsTestConstants;
import com.google.common.base.Predicate;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Copyright 2022
 */
@Log4j2
public class BasePage {

    private static Set<Cookie> cookies;
    private WebDriver driver;
    private WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = WebDriverSingleton.getInstance();
        wait = new WebDriverWait(driver, 20);
    }

    ////////// static methods...
    public static void takeScreenShot() {
        if (WebDriverSingleton.isDriverRunning()) {
            File scrFile =
                ((TakesScreenshot)WebDriverSingleton.getInstance())
                    .getScreenshotAs(OutputType.FILE);
            File targetFile =
                new File(
                    System.getProperty("user.dir")
                        + "\\target\\"
                        + CommonsTestBase.getStartTestTime().toString().replace(":", "-")
                        + ".png");
            try {
                FileUtils.copyFile(scrFile, targetFile);
            } catch (IOException e) {
                log.debug("Failed to take screenshot");
            }
        }
    }

    public static void takeScreenShot(WebElement element) {
        if (WebDriverSingleton.isDriverRunning()) {
            try {
                File screen =
                    ((TakesScreenshot)WebDriverSingleton.getInstance())
                        .getScreenshotAs(OutputType.FILE);
                Point p = element.getLocation();
                Rectangle rect =
                    new Rectangle(element.getSize().getWidth(), element.getSize().getHeight());
                BufferedImage img = ImageIO.read(screen);
                BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
                ImageIO.write(dest, "png", screen);
                FileUtils.copyFile(
                    screen,
                    new File(
                        System.getProperty("user.dir")
                            + "/target/"
                            + CommonsTestBase.getStartTestTime()
                            + ".png"));
            } catch (IOException e) {
                log.debug("Failed to take screenshot");
            }
        }
    }

    public void setScreenSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    public void waitForTitle(String title) {
        waitForLoad();
        wait.until(ExpectedConditions.titleContains(title));
    }

    public void waitForLoad() {
        new WebDriverWait(driver, 30)
            .until(
                    (Predicate<WebDriver>) webDriver ->
                        ((JavascriptExecutor)webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));
    }

    public boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickOn(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        clickOnElement(driver.findElement(locator));
    }

    public void clickOnElement(WebElement element) {
        final int MAX_RETRY_COUNT = 3;
        int attemptCount = 1;

        while (attemptCount <= MAX_RETRY_COUNT) {
            try {
                log.info("Clicking on {} -> attempt {}", element, attemptCount);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                executeJavaScript("arguments[0].click();", element);
                break;
            } catch (TimeoutException | NoSuchElementException e) {
                if (attemptCount >= MAX_RETRY_COUNT) {
                    log.error("Timed out too many times trying to click {}.", element);
                    throw e;
                } else {
                    log.warn("Timeout Exception thrown trying to click {}.", element);
                    attemptCount++;
                }
            }
        }
    }

    public void rightClickOn(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        Actions action = new Actions(getDriver());
        action.contextClick(element).build().perform();
    }

    public void enterData(By element, String data) {
        log.info("Entering: {} to locator: {}", data, element);
        enterTheData(element, data);
    }

    public void enterSensitiveData(By element, String data) {
        log.info("Entering sensitive data: {} to locator: {}", "*".repeat(data.length()), element);
        enterTheData(element, data);
    }

    private void enterTheData(By element, String data) {
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
        driver.findElement(element).sendKeys(data);
    }

    public void clearField(By locator) {
        log.debug("Clearing field: {}", locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        while (!driver.findElement(locator).getAttribute("value").equals("")) {
            driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
        }
    }

    public String getBodyText() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(DOMConstants.BODY)));
        return driver.findElement(By.tagName(DOMConstants.BODY)).getText();
    }

    public void submitForm(By locator) {
        log.info("Submitting form: {}", locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        WebElement form = driver.findElement(locator);
        form.submit();
    }

    public String getAttribute(String attribute, By locator) {
        log.debug("Getting attribute {} for locator {}", attribute, locator);
        waitUntil(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getAttribute(attribute);
    }

    public String getText(By locator) {
        log.debug("Getting text for locator {}", locator);
        waitUntil(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public void hoverOver(By locator) {
        WebElement searchBtn = driver.findElement(locator);
        Actions action = new Actions(driver);
        action.moveToElement(searchBtn).perform();
    }

    public boolean waitForPageSourceText(String... text) {
        return waitForPageSourceText(10, text);
    }

    public boolean waitForPageSourceText(int maxTries, String... text) {
        int counter = 1;
        String pageSourceText = driver.getPageSource();
        while (Arrays.stream(text).noneMatch(pageSourceText::contains) && counter <= maxTries) {
            CommonsTestBase.sleepFor(CommonsTestConstants.A_QUARTER_SECOND);
            pageSourceText = driver.getPageSource();
            counter++;
        }
        if (counter >= maxTries) {
            log.warn("MAXED OUT WAITING FOR PAGE SOUCE TO CONTAIN:{}", text);
            return false;
        } else {
            log.debug("Took:{} tries waiting for pageSourceText:{}", counter, text);
            return true;
        }
    }

    public boolean waitForBodyText(String... text) {
        return waitForBodyText(10, text);
    }

    public boolean waitForBodyText(int maxTries, String... text) {
        int counter = 1;
        String bodyText = this.getBodyText();
        while (Arrays.stream(text).noneMatch(bodyText::contains) && counter <= maxTries) {
            CommonsTestBase.sleepFor(CommonsTestConstants.A_QUARTER_SECOND);
            bodyText = this.getBodyText();
            counter++;
        }
        if (counter >= maxTries) {
            log.warn("MAXED OUT WAITING FOR BODY TO CONTAIN:{}", text);
            return false;
        } else {
            log.debug("Took:{} tries waiting for bodyText:{}", counter, text);
            return true;
        }
    }

    public boolean waitForNotPageSourceText(String text) {
        return waitForNotPageSourceText(10, text);
    }

    public boolean waitForNotPageSourceText(int maxTries, String text) {
        int counter = 1;
        String pageText = driver.getPageSource();
        while (pageText.contains(text) && counter <= maxTries) {
            CommonsTestBase.sleepFor(CommonsTestConstants.A_QUARTER_SECOND);
            pageText = driver.getPageSource();
            counter++;
        }
        if (counter >= maxTries) {
            log.warn("MAXED OUT WAITING FOR PAGE NOT SOURCE:\"{}\"", text);
            return false;
        } else {
            log.debug("Took:{} tries waiting for page NOT source text:\"{}\"", counter, text);
            return true;
        }
    }

    public boolean waitForNotBodyText(String text) {
        return waitForNotBodyText(10, text);
    }

    public boolean waitForNotBodyText(int maxTries, String text) {
        int counter = 1;
        String bodyText = this.getBodyText();
        while (bodyText.contains(text) && counter <= maxTries) {
            CommonsTestBase.sleepFor(CommonsTestConstants.A_QUARTER_SECOND);
            bodyText = this.getBodyText();
            counter++;
        }
        if (counter >= maxTries) {
            log.warn("MAXED OUT WAITING FOR BODY NOT TEXT:\"{}\"", text);
            return false;
        } else {
            log.debug("Took:{} tries waiting for body NOT TEXT text:\"{}\"", counter, text);
            return true;
        }
    }

    public void scrollToBottomOfPage() {
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollToElement(WebElement element) {
        executeJavaScript("arguments[0].scrollIntoView();", element);
    }

    public void waitUntil(ExpectedCondition<WebElement> expectedCondition) {
        this.wait.until(expectedCondition);
    }

    public void waitUntil(int length, ExpectedCondition<WebElement> expectedCondition) {
        this.wait = new WebDriverWait(driver, length);
        try {
            this.wait.until(expectedCondition);
        } catch (TimeoutException e) {
            throw new TimeoutException("Timed out waiting for: " + expectedCondition.toString());
        } finally {
            this.wait = new WebDriverWait(driver, 20);
        }
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public static void setCookies(Set<Cookie> cookiesToAdd) {
        cookies = cookiesToAdd;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void executeJavaScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor)getDriver();
        log.debug("Running javascript command: {}", script);
        log.debug("Javascript args: {}", args);
        js.executeScript(script, args);
    }

}
