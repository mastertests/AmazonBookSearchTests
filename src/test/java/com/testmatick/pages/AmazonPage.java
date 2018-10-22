package com.testmatick.pages;

import com.testmatick.objects.Book;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AmazonPage {
    private final By dropdownBox = By.id("searchDropdownBox");
    private final By searchField = By.id("twotabsearchtextbox");
    private final By submitButton = By.className("nav-input");
    private final WebDriver driver;

    public AmazonPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public List<Book> searchForBooks(String searchWords) {
        driverWait(15);

        driver.findElement(dropdownBox).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[contains(text(), 'Books')]")));

        Select dropdown = getSelect(driver.findElement(dropdownBox));
        dropdown.selectByVisibleText("Books");
        driverWait(5);

        driver.findElement(searchField).sendKeys(searchWords);
        driverWait(5);

        driver.findElement(submitButton).submit();
        driverWait(5);

        List<Book> books = new ArrayList<>();
        for (int i = 0; i < driver.findElements(By.xpath("//li[contains(@class, 's-result-item') and contains(@class, 'celwidget')]")).size(); i++) {
            String dataAsin = driver.findElement(By.xpath("//li[@id=\"result_" + i + "\"]")).getAttribute("data-asin");

            books.add(
                    new Book(
                            driver
                                    .findElement(By.xpath("//li[@id=\"result_" + i + "\"]"))
                                    .findElement(By.xpath(".//h2"))
                                    .getText(),

                            driver
                                    .findElement(By.xpath("//li[@id=\"result_" + i + "\"]"))
                                    .findElement(By.xpath(".//div[contains(@class, 'a-row') and contains(@class, 'a-spacing-none')][2]"))
                                    .getText().substring(3),

                            (double) Integer.valueOf(
                                    driver
                                            .findElement(By.xpath("//li[@id=\"result_" + i + "\"]"))
                                            .findElement(By.xpath(".//div[contains(@class, 'a-column') and contains(@class, 'a-span7')]"))
                                            .findElement(By.xpath(".//span[contains(@aria-hidden, 'true') " +
                                                    "and contains(@class, 'a-color-base') and contains(@class, 'sx-zero-spacing')]"))
                                            .findElement(By.xpath(".//span[contains(@class, 'sx-price') and contains( @class, 'sx-price-large')]"))
                                            .getText().replace(" ", "").substring(1)
                            ) / 100,

                            setRatio(dataAsin),

                            isElementPresent(By.id("BESTSELLER_" + dataAsin))
                    )
            );
        }
        return books;
    }

    private Select getSelect(WebElement element) {
        return new Select(element);
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private String setRatio(String dataAsin) {
        String result = "No ratio";
        if (isElementPresent(By.xpath("//span[contains(@name, '" + dataAsin + "')]"))) {
            result = driver
                    .findElement(
                            By.xpath("//span[contains(@data-a-popover,'{\"max-width\":\"700\",\"closeButton\":\"false\",\"position\":\"triggerBottom\",\"url\":\"/review/widgets/average-customer-review/popover/ref=acr_search__popover?ie=UTF8&asin=" +
                                    dataAsin + "&contextId=search&ref=acr_search__popover\"}')]"))
                    .getText().substring(0, 3);
        }
        return result;
    }

    private void driverWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }
}

