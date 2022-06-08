package com.allenc.selenium.page.google;

import com.allenc.selenium.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GoogleSearchPage extends BasePage {

    public By searchField = By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/input");
    public By badLink = By.xpath("bad/link");
    public By searchButton = By.className("gNO89b");
    public GoogleSearchPage(WebDriver driver) {
        super(driver);
        setScreenSize(1500, 768);
    }

    public GoogleSearchResultsPage searchFor(String searchQuery) {
        enterData(searchField, searchQuery);
        submitForm(searchButton);
        waitForTitle(searchQuery + " - Google Search");
        return new GoogleSearchResultsPage(getDriver());
    }

}
