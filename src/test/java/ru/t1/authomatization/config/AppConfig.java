package ru.t1.authomatization.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

// Указываем источник конфигурации
@Config.LoadPolicy(Config.LoadType.MERGE)
@Sources({
        "system:properties",
        "system:env",
        "classpath:config/application-${app.env}.properties",
        "classpath:config/application.properties"})
public interface AppConfig extends Config {
    @Key("stand.url")
    @DefaultValue("https://example.com")
    String standUrl();

    // Stand API
    @Key("stand.api")
    @DefaultValue("/api/v1")
    String standApi();

    // Timeout
    @Key("timeout")
    @DefaultValue("10")
    int timeout();

    // Logging Level
    @Key("logging.level")
    @DefaultValue("INFO")
    String loggingLevel();

    // Credentials Username
    @Key("credentials.username")
    @DefaultValue("admin")
    String username();

    // Credentials Password
    @Key("credentials.password")
    String password();

    // Product Name
    @Key("product.name")
    @DefaultValue("Laptop Pro")
    String productName();

    // Product Price
    @Key("product.price")
    @DefaultValue("0.0")
    double productPrice();
}
