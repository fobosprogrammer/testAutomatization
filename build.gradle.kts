import java.time.Duration

// Плагины расширяют возможности Gradle
plugins {
    id("java")          // компиляция Java, тесты, jar
    id("application")   // запуск приложения и создание дистрибутивов
}

group = "ru.t1.authomatization"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Главный класс для `gradle run`
application {
    mainClass = "ru.t1.authomatization.Main"
}
// Зависимости: testImplementation — только для тестов
dependencies {
    // =====================================================================
    //  SELENIDE — библиотека для UI-автотестов (пакет ru.stepup.ui)
    // =====================================================================
    // Selenide — обёртка над WebDriver: сам скачивает драйвер браузера
    // (через встроенный WebDriverManager), сам управляет таймаутами и
    // ожиданиями (умные wait'ы вместо sleep), умеет делать скриншоты
    // и сохранять HTML при падении теста.
    //
    // Основные возможности, которые используем в вебинаре:
    //   - $("css") / $$("css") / $x("//xpath") — поиск элементов;
    //   - shouldBe / shouldHave(Condition)     — «умные» ожидания;
    //   - open("url")                          — открытие страницы;
    //   - PageObject-паттерн (пакет ru.stepup.ui.pageobject);
    //   - интеграция с JUnit 5: @BeforeEach/@AfterEach.
    //
    // Документация: https://selenide.org
    // Версию поднять: поменять номер тут и перезапустить ./gradlew build.
    implementation("com.codeborne:selenide:7.17.0")
    // =====================================================================
    //  AEONBITS.OWNER — типизированная работа с properties (вебинар «Конфигурирование»)
    // =====================================================================
    // owner позволяет «спроецировать» .properties-файл на Java-интерфейс:
    // вместо ручного Properties.getProperty() + парсинга типов мы объявляем
    // методы интерфейса (int port(), String host()) и получаем значения
    // сразу нужного типа. Документация и примеры: https://owner.aeonbits.org
    // Версия 1.0.12 — последний стабильный релиз из Maven Central.
    // Все учебные примеры лежат в пакетах ru.stepup.config.*
    implementation("org.aeonbits.owner:owner:1.0.12")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // AssertJ — «текучие» (fluent) проверки с читаемыми сообщениями об ошибках.
    // Примеры использования лежат в тестах AssertJ_*Test.
    testImplementation("org.assertj:assertj-core:3.27.7")
    // =====================================================================
    //  REST API-тестирование (пакет ru.stepup.api)
    // =====================================================================
    // RestAssured — DSL для тестирования REST API: позволяет описывать HTTP-запрос
    // (метод, заголовки, тело, авторизацию) и проверки ответа в стиле «given/when/then».
    // Документация: https://rest-assured.io
    testImplementation("io.rest-assured:rest-assured:5.5.2")
    // Jackson — сериализация/десериализация JSON в Java-объекты (DTO).
    // RestAssured сам подхватывает Jackson из classpath для методов response.as(Class) /
    // jsonPath().getList(...). ВАЖНО: должна быть объявлена как testImplementation,
    // иначе Jackson не увидит наш package (иерархия загрузчиков классов Gradle).
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    systemProperties(
        mapOf(
            "api.username" to project.findProperty("api.username"),
            "api.password" to project.findProperty("api.password"),
            "api.base.uri" to project.findProperty("api.base.uri"),
            "api.port" to project.findProperty("api.port")
        ))
}
// ---------- 5а. REST API-тесты (RestAssured) ----------
// Тесты на ручки HTTP-сервиса (см. пакет ru.stepup.api) отбираются по тегу @Tag("api").
// Они требуют ЗАПУЩЕННЫЙ сервер (по умолчанию http://127.0.0.1:8080, см. класс Endpoints).
val apiTest by tasks.register<Test>("apiTest") {
    group = "verification"
    description = "Runs REST API tests with RestAssured (JUnit tag 'api'). Needs a running server!"

    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath

    useJUnitPlatform {
        includeTags("api")
    }

    // Пробрасываем конфигурацию в тестовую JVM как системные свойства.
    // Источник значений (по приоритету):
    //   1) -Dapi.xxx=...  — системное свойство из командной строки (./gradlew apiTest -Dapi.username=...)
    //   2) -PapiXxx=...   — gradle-свойство из командной строки (./gradlew apiTest -PapiUsername=...)
    //   3) значение по умолчанию
    // Тонкость: Gradle НЕ передаёт системные свойства из своей JVM в тестовую
    // автоматически — поэтому явно читаем их через providers.systemProperty
    // и кладём в тестовую JVM через systemProperty(...).
    systemProperty("api.base.uri", providers.systemProperty("api.base.uri")
        .orElse(providers.gradleProperty("apiBaseUri"))
        .orElse("http://127.0.0.1")
        .get())
    systemProperty("api.port", providers.systemProperty("api.port")
        .orElse(providers.gradleProperty("apiPort"))
        .orElse("8080")
        .get())
    systemProperty("api.username", providers.systemProperty("api.username")
        .orElse(providers.gradleProperty("apiUsername"))
        .orElse("admin")
        .get())
    systemProperty("api.password", providers.systemProperty("api.password")
        .orElse(providers.gradleProperty("apiPassword"))
        .get())

    // Показываем вывод тестовой JVM в консоли (stdout/stderr).
    // Без этого Gradle «глотает» логи RestAssured из .log().all() —
    // они уходят в build/reports, но не видны в терминале.
    testLogging {
        showStandardStreams = true
        events("passed", "failed", "skipped")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

// ---------- 8. Запустить все группы одним заходом ----------
tasks.register("runAllTests") {
    group = "verification"
    description = "Runs all test groups: unit, integration, slow, smoke"
    dependsOn("test", "integrationTest", "slowTest", "smokeTest")
}