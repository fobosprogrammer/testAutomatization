package ru.t1.authomatization.ui.homework6;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import static com.codeborne.selenide.Configuration.baseUrl;

public class AdminPage {
    private WebDriver driver;
    private String loginUrl = "/login";
    private String addProductUrl = "/admin";
    private static final String username = System.getProperty("api.username");
    private static final String password = System.getProperty("api.password");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openLoginPage() {
        driver.get(baseUrl + loginUrl);
    }

    public void login(String usernameStr, String passwordStr) {
        WebElement userField = driver.findElement(By.id("username"));
        userField.clear();
        userField.sendKeys(usernameStr);

        WebElement passField = driver.findElement(By.id("password"));
        passField.clear();
        passField.sendKeys(passwordStr);

        driver.findElement(By.cssSelector("button.primary")).click();
    }
    public void login() {
        login(username,password);

    }

    public void addProduct(String productName, String price) {
        driver.get(baseUrl + addProductUrl);

        // Скриншот текущей страницы
//        TakesScreenshot ts = (TakesScreenshot) driver;
//        File source = ts.getScreenshotAs(OutputType.FILE);
//// Сохраните файл, чтобы посмотреть, что видит браузер
//// new File("src/test/resources/screenshots/error.png").createNewFile();
//// FileUtils.copyFile(source, new File("path/to/save/screenshot.png"));
//
//// Вывод текущего URL
//        System.out.println("Current URL: " + driver.getCurrentUrl());
//
//// Вывод заголовка страницы
//        System.out.println("Page Title: " + driver.getTitle());
//
//// Вывод полного HTML страницы (для анализа структуры)
//        System.out.println("Page Source: " + driver.getPageSource());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
// Ждем, пока элемент станет видимым
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("n-name")));
        nameField.clear();
        nameField.sendKeys(productName);

        WebElement priceField = driver.findElement(By.id("n-price"));
        priceField.clear();
        priceField.sendKeys(price);

        driver.findElement(By.id("add-btn")).click();
    }

    public void deleteProduct(String productName) {
        openLoginPage();
        login();
        // Создаем явное ожидание
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1. Ищем строку таблицы (<tr>), внутри которой есть input с нужным значением.
            // XPath объяснение:
            // //tbody//tr - ищем tr внутри tbody
            // [contains(.//input[@type='text']/@value, '" + productName + "')] - проверяем,
            // что где-то внутри строки есть input типа text, у которого value содержит искомое имя.
            // Мы используем contains, чтобы избежать проблем с точным совпадением, если вдруг есть лишние пробелы,
            // но для надежности лучше использовать точное совпадение, если данные чистые.

            // Вариант с точным совпадением значения value:
            String xpathToRow = "//tbody//tr[.//input[@type='text' and @value='" + productName + "']]";

            WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathToRow)));

            // 2. Внутри найденной строки ищем кнопку удаления.
            // Ищем кнопку с классом btn-del или data-action="delete"
            WebElement deleteButton = row.findElement(By.cssSelector("button.btn-del[data-action='delete']"));

            // Альтернативный поиск кнопки через XPath внутри строки:
            // WebElement deleteButton = row.findElement(By.xpath(".//button[@data-action='delete']"));

            // 3. Кликаем по кнопке удаления
            deleteButton.click();

            System.out.println("Продукт '" + productName + "' успешно удален.");

        } catch (Exception e) {
            System.err.println("Не удалось найти или удалить продукт с именем: " + productName);
            e.printStackTrace();
        }
    }
}
