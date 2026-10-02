package ru.t1.authomatization.ui.homework6;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static com.codeborne.selenide.Configuration.baseUrl;

public class StorePage {
    private WebDriver driver;
    private String homeUrl = "/";
    private String cartUrl = "/cart";

    public StorePage(WebDriver driver) {
        this.driver = driver;
    }

    public void openHome() {
        driver.get(baseUrl + homeUrl);
    }

    public void openCart() {
        driver.findElement(By.id("open-cart-btn")).click();
    }

    public boolean checkProductInCart(String productName) {
        try {
            // Ожидание появления элемента с товаром в корзине
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement productElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='cart-item' and contains(., '" + productName + "')]")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Метод для клика по товару на витрине
    public void clickProductOnVitrine(String productName) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement productLink = driver.findElement(By.xpath("//button[@data-name='" + productName + "']"));
        productLink.click();
    }
}
