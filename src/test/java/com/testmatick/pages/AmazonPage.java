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
    private final By bookName = By.xpath("s-item-container//h2");//By.className("h2.a-size-medium.s-inline");
    private final By bookAuthor = By.cssSelector(".a-row.a-spacing-none");
    private final By bookPriceWhole = By.className("sx-price-whole");
    private final By bookPriceFractional = By.className("sx-price-fractional");
    private final By bookRatio = By.cssSelector(".a-size-base.a-color-secondary");
    private final By bookBestSeller = By.linkText("Best Seller");
    private final WebDriver driver;

    public AmazonPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public List<Book> searchForBooks(String searchWords) {
        driver.findElement(dropdownBox).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        Select dropdown = getSelect(driver.findElement(dropdownBox));
        dropdown.selectByVisibleText("Books");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(searchField).sendKeys(searchWords);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(submitButton).submit();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        List<WebElement> bookItems = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < driver.findElements(bookName).size(); i++)
            bookItems.add(driver.findElement(By.id("result_" + i)));

        for (WebElement bookItem : bookItems)
            books.add(
                    new Book(
                            bookItem.findElement(bookName).getAttribute("data-attribute"),
                            bookItem.findElement(bookAuthor).findElement(By.className("a.a-link-normal.a-text-normal")).getText(),
                            Double.valueOf(
                                    bookItem.findElement(bookPriceWhole).getText() + "." +
                                            bookItem.findElement(bookPriceFractional).getText()
                            ),
                            bookItem.findElement(bookRatio).getText(),
                            bookItem.findElements(bookBestSeller).size() > 0
                    )
            );

        return books;
    }

    private Select getSelect(WebElement element) {
        return new Select(element);
    }

}

