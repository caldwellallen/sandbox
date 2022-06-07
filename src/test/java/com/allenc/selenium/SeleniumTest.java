package com.allenc.selenium;

import static com.allenc.selenium.BasePage.setCookies;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.allenc.TestBase;
import com.allenc.selenium.page.google.GoogleSearchPage;
import com.allenc.selenium.page.google.GoogleSearchResultsPage;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * @author Copyright 2022
 */
class SeleniumTest extends TestBase {

    private GoogleSearchPage googleSearchPage;

    @BeforeEach
    void getGoogleSearchPage() {
        runTestsAsSelenium();
        getDriver().get("https://www.google.com/");
        googleSearchPage = new GoogleSearchPage(getDriver());
        googleSearchPage.waitForLoad();
    }

    @Test
    void canEnterDataAndSubmitForm() {
        googleSearchPage.enterData(googleSearchPage.searchField, "doxo");
        googleSearchPage.submitForm(googleSearchPage.searchButton);
        GoogleSearchResultsPage searchResultsPage = new GoogleSearchResultsPage(getDriver());
        searchResultsPage.waitForTitle("doxo - Google Search");
        assertThat(searchResultsPage.getBodyText()).contains("What bills can I pay with doxo?");
    }

    @Test
    void canEnterSensitiveDataAndSubmitForm() {
        googleSearchPage.enterSensitiveData(googleSearchPage.searchField, "doxo");
        googleSearchPage.submitForm(googleSearchPage.searchButton);
        GoogleSearchResultsPage searchResultsPage = new GoogleSearchResultsPage(getDriver());
        searchResultsPage.waitForTitle("doxo - Google Search");
        assertThat(searchResultsPage.getBodyText()).contains("What bills can I pay with doxo?");
    }

    @Test
    void canHoverOver() {
        googleSearchPage.enterSensitiveData(googleSearchPage.searchField, "doxo");
        googleSearchPage.hoverOver(googleSearchPage.searchButton);
        Actions actions = new Actions(getDriver());
        actions.click().build().perform();
        GoogleSearchResultsPage searchResultsPage = new GoogleSearchResultsPage(getDriver());
        searchResultsPage.waitForTitle("doxo - Google Search");
        assertThat(searchResultsPage.getBodyText()).contains("What bills can I pay with doxo?");
    }

    @Test
    void canGetPageSource() {
        assertThat(googleSearchPage.getPageSource()).contains("<html");
    }

    @Test
    void canWaitForPageSourceText() {
        assertThat(googleSearchPage.waitForPageSourceText("Welcome")).isTrue();
        assertThat(googleSearchPage.waitForNotPageSourceText("XXXXXXXXXXXX")).isTrue();
        assertThat(googleSearchPage.waitForPageSourceText("XXXXXXXXXXXX")).isFalse();
        assertThat(googleSearchPage.waitForNotPageSourceText("Welcome")).isFalse();
    }

    @Test
    void canWaitForBodyText() {
        assertThat(googleSearchPage.waitForBodyText("Welcome")).isTrue();
        assertThat(googleSearchPage.waitForNotBodyText("XXXXXXXXXXXX")).isTrue();
        assertThat(googleSearchPage.waitForBodyText("XXXXXXXXXXXX")).isFalse();
        assertThat(googleSearchPage.waitForNotBodyText("Welcome")).isFalse();
    }

    @Test
    void canSetAndGetCookies() {
        setCookies(Sets.newHashSet(new Cookie("test", "testThis")));
        assertThat(googleSearchPage.getCookies()).isNotNull();
    }

    @Test
    void waitUntilThrowsException() {
        Throwable thrown = catchThrowable(() -> googleSearchPage.waitUntil(2, ExpectedConditions.presenceOfElementLocated(googleSearchPage.badLink)));
        assertThat(thrown).isInstanceOf(org.openqa.selenium.TimeoutException.class);
    }

}
