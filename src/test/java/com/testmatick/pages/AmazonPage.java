package com.testmatick.pages;

import com.testmatick.objects.Book;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AmazonPage {
    private final By dropdownBox = By.id("searchDropdownBox");
    private final By searchField = By.id("twotabsearchtextbox");
    private final By submitButton = By.className("nav-input");
    private final By resultItem = By.className("s-result-item");
    private final String dropdownItemValue = "search-alias=stripbooks";
    private final WebDriver driver;

    public AmazonPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public List<Book> searchForBooks(String searchWords) {
        driver.findElement(dropdownBox).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Select dropdown = getSelect(driver.findElement(dropdownBox));
        dropdown.selectByValue(dropdownItemValue);
        driver.findElement(searchField).sendKeys(searchWords);
        driver.findElement(submitButton).submit();
        List<WebElement> bookItems = driver.findElements(resultItem);
        List<Book> books = new ArrayList<Book>();
        for (WebElement bookItem : bookItems) {
            books.add(
                    new Book(
                            bookItem.findElement(By.cssSelector("h2.a-size-medium.s-inline")).getText(),
                            bookItem.findElement(By.cssSelector("a.a-link-normal.a-text-normal")).getText(),
                            Double.valueOf(
                                    bookItem.findElement(By.className("sx-price-whole")).getText() + "." +
                                            bookItem.findElement(By.className("sx-price-fractional"))
                            ),
                            bookItem.findElement(By.cssSelector("span.a-size-base.a-color-secondary")).getText(),
                            bookItem.findElement(By.cssSelector("span.a-badge-label-inner.a-text-ellipsis")).getText().equals("Best Seller")
                    )
            );
        }
        return books;
    }

    private Select getSelect(WebElement element) {
        return new Select(element);
    }
}

