package com.testmatick.pages;

import com.testmatick.objects.Book;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
    private final WebDriver driver;

    public AmazonPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public List<Book> searchForBooks(String searchWords) {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(dropdownBox).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        Select dropdown = getSelect(driver.findElement(dropdownBox));
        dropdown.selectByVisibleText("Books");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(searchField).sendKeys(searchWords);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(submitButton).submit();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        List<Book> books = new ArrayList<>();
        for (int i = 0; i < driver.findElements(By.xpath("//li[contains(@class, 's-result-item') and contains(@class, 'celwidget')]")).size(); i++) {
            String name = driver.findElement(By.xpath("//li[@id=\"result_" + i + "\"]")).getAttribute("data-asin");
            Book book = new Book(
                    driver
                            .findElement(By.xpath("//li[@id=\"result_" + i + "\"]"))
                            .findElement(By.xpath(".//h2"))
                            .getText(),

                    driver
                            .findElement(By.xpath("//li[@id=\"result_" + i + "\"]"))
                            .findElement(By.xpath(".//div[contains(@class, 'a-row') and contains(@class, 'a-spacing-none')][2]"))
                            .findElement(By.xpath(".//span[contains(@class, 'a-size-small') and contains(@class, 'a-color-secondary')][2]"))
                            .getText(),

//                    (double) Integer.valueOf(
//                            driver
//                                    .findElement(By.xpath("//li[@id=\"result_" + i + "\"]"))
//                                    .findElement(By.xpath(".//div[contains(@class, 'a-column') and contains(@class, 'a-span7')]"))
//                                    .findElement(By.xpath(".//div[contains(@class, 'a-row') and contains(@class, 'a-spacing-none') " +
//                                            "and .//a[contains(@title, 'Paperback')]]/following::div[1]"))
//                                    .findElement(By.xpath(".//a[contains(@class,'a-link-normal') and contains(@class, 'a-text-normal')] "))
//                                    .getText().substring(1).replace(" ", "")
//                    ) / 100 ,
0.0,
                    driver
                            .findElement(By.xpath("//li[@id=\"result_" + i + "\"]"))
                            .findElement(By.xpath(".//div[contains(@class, 'a-column') and contains(@class, 'a-span5')" +
                                    "and contains(@class, 'a-span-last')]"))
                            .findElement(By.xpath(".//span[contains(@name, '" + name + "')]"))
                            .findElement(By.xpath(".//span[contains(@class, 'a-icon-alt')]"))
                            .getText(),

                    isElementPresent(By.id("BESTSELLER_" + name))
            );
            books.add(book);
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
}

