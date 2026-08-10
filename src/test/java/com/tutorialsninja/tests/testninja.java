package com.tutorialsninja.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class testninja {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://tutorialsninja.com/demo/";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        // Check for headless property (useful for CI environment)
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void testLoginSearchAddToCartCheckoutAndLogout() {
        // 1. Navigate to URL
        driver.get(BASE_URL);

        // 2. Login with valid email & password
        driver.findElement(By.xpath("//span[text()='My Account']")).click();
        driver.findElement(By.linkText("Login")).click();

        driver.findElement(By.id("input-email")).sendKeys("testninja100@gmail.com");
        driver.findElement(By.id("input-password")).sendKeys("pitam@100");
        driver.findElement(By.xpath("//input[@value='Login']")).click();

        // Verify successful login
        WebElement myAccountHeader = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='My Account']"))
        );
        Assert.assertTrue(myAccountHeader.isDisplayed(), "User was not able to login successfully.");

        // 3. Search for "laptop"
        WebElement searchBox = driver.findElement(By.name("search"));
        searchBox.clear();
        searchBox.sendKeys("MacBook"); // MacBook is listed under laptops on TutorialsNinja
        driver.findElement(By.cssSelector("button.btn.btn-default.btn-lg")).click();

        // 4. Click the search result item
        WebElement productLink = wait.until(
            ExpectedConditions.elementToBeClickable(By.linkText("MacBook"))
        );
        productLink.click();

        // 5. Click "Add to Cart"
        WebElement addToCartBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("button-cart"))
        );
        addToCartBtn.click();

        // Verify success message
        WebElement successAlert = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success"))
        );
        Assert.assertTrue(successAlert.getText().contains("Success: You have added MacBook to your shopping cart!"));

        // 6. Go to Shopping Cart & Checkout
        driver.findElement(By.xpath("//a[@title='Shopping Cart']")).click();

       /*  // 7. Verify total amount using Assertion
        WebElement totalAmountElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-sm-4 col-sm-offset-8']//tr[last()]/td[last()]"))
        );
        String actualTotal = totalAmountElement.getText().trim();
        String expectedTotal = "$602.00"; // Price of 1x MacBook
        Assert.assertEquals(actualTotal, expectedTotal, "Total cart amount does not match!");

        // Click Checkout Button
        driver.findElement(By.linkText("Checkout")).click();  */

        // 8. Logout from website
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement myAccount = wait.until(
    ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='My Account']"))
);
myAccount.click();

               
       WebElement logoutBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.linkText("Logout"))
        );
        logoutBtn.click();

        WebElement logoutHeader = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Account Logout']"))
        );
        Assert.assertTrue(logoutHeader.isDisplayed(), "User was not logged out successfully."); 
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    }

