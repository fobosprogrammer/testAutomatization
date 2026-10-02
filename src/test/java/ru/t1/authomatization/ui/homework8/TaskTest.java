package ru.t1.authomatization.ui.homework8;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.t1.authomatization.config.AppConfig;
import org.aeonbits.owner.ConfigFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {
    protected static AppConfig config;

    @BeforeAll
    public static void loadConfig() {
        // 1. Загрузка конфигурации из application.properties
        // Owner автоматически найдет файл в classpath или по указанному пути
        config = ConfigFactory.create(AppConfig.class);

        // 2. Вывод параметров в консоль перед запуском теста
        System.out.println("=== Configuration Loaded via Owner ===");

        // URL стенда и API
        System.out.println("Stand URL: " + config.standUrl());
        System.out.println("API Endpoint: " + config.standApi());

        // Тайм-аут
        System.out.println("Element Timeout: " + config.timeout() + " seconds");

        // Режим логирования
        System.out.println("Logging Level: " + config.loggingLevel());

        // Имя и цена товара
        System.out.println("Start Product Name: " + config.productName());
        System.out.println("Start Product Price: " + config.productPrice());

        // Credentials НЕ выводятся в консоль согласно требованию
        // Мы можем использовать их внутри кода, но не печатать
        // String user = config.username();
        // String pass = config.password();

        System.out.println("============================");
    }

    @Test
    void testLoad() {
        assertTrue(true);
    }
}
