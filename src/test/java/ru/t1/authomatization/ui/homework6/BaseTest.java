package ru.t1.authomatization.ui.homework6;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl = "http://localhost:8080"; // Замените на URL вашего сайта

    @BeforeEach
    public void setUp() {
        // Инициализация драйвера (пункт 1.5)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Опционально: запуск без GUI
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        this.driver = new ChromeDriver(options);
        this.driver.manage().window().maximize();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        // Закрытие браузера (пункт 1.5)
        if (driver != null) {
            driver.quit();
        }
    }
}
