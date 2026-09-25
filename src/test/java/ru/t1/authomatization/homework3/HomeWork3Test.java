package ru.t1.authomatization.homework3;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.t1.authomatization.homework1.BaseJava;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class HomeWork3Test {
    // Задача 1: 4 автотеста с ассертами

    @Test
    void isEven_returnsTrueForEvenNumber() {
        boolean result = BaseJava.isEven(42);
        assertTrue(result, "Ожидалось true для четного числа 42, получено: " + result);
    }

    @Test
    void isEven_returnsFalseForOddNumber() {
        boolean result = BaseJava.isEven(43);
        assertFalse(result, "Ожидалось false для нечетного числа 43, получено: " + result);
    }


    @Test
    void isEven_checkAsserFailed() {
        int number = 42;
        boolean result = BaseJava.isEven(number);
        assertFalse(result, "Ожидалось false для нечетного числа " + number + ", получено: " + result);
    }

    @Test
    void reverse_returnsReversedArray() {
        String[] input = {"A", "B", "C"};
        String[] expected = {"C", "B", "A"};
        String[] result = BaseJava.reverse(input);
        assertArrayEquals(expected, result, "Ожидался массив " + Arrays.toString(expected) + ", получен: " + Arrays.toString(result));
    }

    @Test
    void getGrade_returnsErrorForInvalidScore() {
        String result = BaseJava.getGrade(150);
        assertEquals("Error", result, "Ожидалось 'Error' для неверного балла 150, получено: " + result);
    }

    // Задача 2: Дополнение тестов из задачи 2 с ассертами и фильтрацией по тегам

    @Tag("fast")
    @RepeatedTest(10)
    void checkAccess_withValidAge() {
        int age = 25;
        String result = BaseJava.checkAccess(age);
        assertEquals("ACCESS_GRANTED", result, "Ожидалось 'ACCESS_GRANTED' для возраста " + age + ", получено: " + result);
    }

    @Tag("fast")
    @RepeatedTest(10)
    void checkAccess_withInvalidAge() {
        int age = 16;
        String result = BaseJava.checkAccess(age);
        assertEquals("ACCESS_DENIED", result, "Ожидалось 'ACCESS_DENIED' для возраста " + age + ", получено: " + result);
    }

    @Tag("parametrized")
    @ParameterizedTest
    @ValueSource(ints = {0, 20, 21, 40, 41, 60, 61, 80, 81, 100})
    void getGrade_withValidScores(int score) {
        String expected;
        if (score <= 20) expected = "E";
        else if (score <= 40) expected = "D";
        else if (score <= 60) expected = "C";
        else if (score <= 80) expected = "B";
        else expected = "A";

        String result = BaseJava.getGrade(score);
        assertEquals(expected, result, "Ожидалась оценка '" + expected + "' для балла " + score + ", получено: " + result);
    }

    @Tag("edge")
    @RepeatedTest(10)
    void reverse_withNullInput() {
        String[] result = BaseJava.reverse(null);
        assertNull(result, "Ожидался null при передаче null, получено: " + result);
    }

    @Tag("edge")
    @RepeatedTest(10)
    void reverse_withEmptyArray() {
        String[] input = {};
        String[] result = BaseJava.reverse(input);
        assertNotNull(result, "Ожидался не-null массив при пустом входе");
        assertEquals(0, result.length, "Ожидался массив длины 0, получено: " + result.length);
    }

    @Tag("slow")
    @RepeatedTest(10)
    void getGrade_withNegativeScore() {
        int score = -5;
        String result = BaseJava.getGrade(score);
        assertEquals("Error", result, "Ожидалось 'Error' для отрицательного балла " + score + ", получено: " + result);
    }

    @Tag("slow")
    @RepeatedTest(10)
    void getGrade_withOverHundredScore() {
        int score = 105;
        String result = BaseJava.getGrade(score);
        assertEquals("Error", result, "Ожидалось 'Error' для балла >100: " + score + ", получено: " + result);
    }

    @Tag("integration")
    @RepeatedTest(10)
    void removeSpecificName_removesAllOccurrences() {
        List<String> input = Arrays.asList("Alice", "Bob", "Charlie", "Bob", "Diana");
        List<String> expected = Arrays.asList("Alice", "Charlie", "Diana");
        List<String> result = BaseJava.removeSpecificName(input, "Bob");
        assertEquals(expected, result, "Ожидался список без 'Bob': " + expected + ", получено: " + result);
    }

    @Tag("integration")
    @RepeatedTest(10)
    void removeSpecificName_withNullList() {
        List<String> result = BaseJava.removeSpecificName(null, "Bob");
        assertNotNull(result, "Ожидался не-null список при null-входе, получено: " + result);
        assertTrue(result.isEmpty(), "Ожидался пустой список, получено: " + result.size());
    }
}
