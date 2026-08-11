package ru.t1.authomatization.homework2;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.t1.authomatization.homework1.BaseJava;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HomeWork2Test {
    private static final Random random = new Random();

    @BeforeEach
    void BeforeEach(TestInfo testInfo) {
        String methodName = testInfo.getTestMethod()
                .map(method -> method.getName())
                .orElse("Unknown Method");
        System.out.println("========================");
        System.out.println("Test method start");
        System.out.println("Method name - " + methodName);
    }

    @AfterEach
    void AfterEach() {
        System.out.println("Test method end");
        System.out.println("========================");
    }

    @Test
    void testIsEven() {
        int number = random.nextInt(99) + 1;
        boolean result = BaseJava.isEven(number);
        assertNotNull(result);
    }

    @RepeatedTest(20)
    void testCheckAccess() {
        int age = random.nextInt(99);
        String result = BaseJava.checkAccess(age);
        assertNotNull(result);

    }

    @ParameterizedTest
    @CsvSource({
            "99, A",
            "79, B",
            "59, C",
            "39, D",
            "19, E",
            "120, Error",
            "-1, Error",
            "101, Error",
    })
    void testGetGrade(int score, String expected) {
        String result = BaseJava.getGrade(score);
        assertEquals(expected, result);
    }

    @Test
    void getGrade_randomValues() {
        Random random = new Random();
        for (int score = 0; score < 101; score++) {
            String result = BaseJava.getGrade(score);
            String expected;
            if (score >= 0 && score <= 20) expected = "E";
            else if (score <= 40) expected = "D";
            else if (score <= 60) expected = "C";
            else if (score <= 80) expected = "B";
            else if (score <= 100) expected = "A";
            else expected = "Error";
            assertEquals(expected, result, "Score: " + score);
        }
    }
}
