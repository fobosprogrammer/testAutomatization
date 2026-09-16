package ru.t1.authomatization.api.homework4;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    private static final String baseUrl = System.getProperty("api.base.uri");
    private static final String port = System.getProperty("api.port");
    private static final String username = System.getProperty("api.username");
    private static final String password = System.getProperty("api.password");
    @BeforeAll
    static void setup() {
        // Настройка базового URI и порта
        RestAssured.baseURI = baseUrl + ":" + port;

        // Если API требует Basic Auth, как указано в Swagger (security: basicAuth)
        // В реальном проекте лучше выносить логины/пароли в конфиг или env vars
        RestAssured.authentication = RestAssured.basic("user", "password");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
