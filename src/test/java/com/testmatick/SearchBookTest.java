package com.testmatick;

import com.testmatick.objects.Book;
import com.testmatick.pages.AmazonPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

public class SearchBookTest {
    private WebDriver driver;

    @Parameters("browser")
    @BeforeTest
    protected void setUp(String browser) {
        switch (browser) {
            default:
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src/test/java/com/testmatick/drivers/chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", "src/test/java/com/testmatick/drivers/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            case "edge":
                System.setProperty("webdriver.edge.driver", "src/test/java/com/testmatick/drivers/MicrosoftWebDriver.exe");
                driver = new EdgeDriver();
                break;
        }
        driver.manage().window().maximize();
    }

    @Test
    @Parameters("search")
    protected void testAmazonSearch(String searchWords) {
        driver.get("https://www.amazon.com/");
        AmazonPage page = new AmazonPage(driver);
        List<Book> books = page.searchForBooks(searchWords);
        boolean isExist = false;
        for (Book book : books)
            if (book.getName().equals("Head First Java, 2nd Edition"))
                isExist = true;

        if (isExist)
            System.out.println("Head First Java, 2nd Edition is on the list");
        else
            System.out.println("Head First Java, 2nd Edition is NOT on the list");
    }

    @AfterTest
    protected void tearDown() {
        driver.quit();
    }
}
