package ru.t1.authomatization.homework2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.t1.authomatization.homework1.BaseJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class BaseJavaTest {

    private static final Random random = new Random();

    @Test
    @DisplayName("Задача 1")
    void isEven() {
        int n = random.nextInt(100) + 1;
        boolean result = BaseJava.isEven(n);
        boolean expected = n % 2 == 0;
        System.out.println(result == expected ? "TEST PASSED" : "TEST FAILED");
    }

    @RepeatedTest(10)
    @DisplayName("Задача 2")
    void checkAccess() {
        Random random = new Random();

        int age = random.nextInt(99) + 1;
        String result = BaseJava.checkAccess(age);
        String expected = age >= 18 ? "ACCESS_GRANTED" : "ACCESS_DENIED";
        System.out.println(result.equals(expected) ? "TEST PASSED" : "TEST FAILED");
    }

    @RepeatedTest(10)
    @DisplayName("Задача 3")
    void isPositive() {
        Random random = new Random();
        int number = random.nextInt(199) - 99;
        Boolean result = BaseJava.isPositive(number);
        System.out.println(number >= 0 && result ? "TEST PASSED" : "TEST FAILED");
    }

    @DisplayName("Задача 4")
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
    void getGrade(int score,String expectedGrade) {
        String result = BaseJava.getGrade(score);
        String expected;
        if (score <= 20) expected = "E";
        else if (score <= 40) expected = "D";
        else if (score <= 60) expected = "C";
        else if (score <= 80) expected = "B";
        else if (score <= 100) expected = "A";
        else expected = "Error";

        System.out.println(result.equals(expected) ? "TEST PASSED" : "TEST FAILED");
    }

    @DisplayName("Задача 5")
    @ParameterizedTest
    @CsvSource({
            "5, 5 4 3 2 1 Поехали!",
            "4, 4 3 2 1 Поехали!"
    })
    void blastOff(int number,String expectedString) {
        String result = BaseJava.blastOff(number);
        System.out.println(result.equals(expectedString) ? "TEST PASSED" : "TEST FAILED");
    }

    @RepeatedTest(10)
    @DisplayName("Задача 6")
    void sumToN() {
        Random random = new Random();
        int number = random.nextInt(20);
        System.out.println((number * (number + 1) / 2 == BaseJava.sumToN(number)) ? "TEST PASSED" : "TEST FAILED");
    }

    @ParameterizedTest
    @DisplayName("Задача 7")
    @CsvSource(value = {
            "false, elem1; elem2; elem3",
            "true,  Bug;   elem2; elem3",
            "true,  elem1; bug;  elem3",
            "true,  elem1; BUG;  elem3"
    })
    void hasBug(boolean expected, String elementsList) {
        String[] arr = Arrays.stream(elementsList.split(";"))
                .map(String::trim)
                .toArray(String[]::new);
        boolean result = BaseJava.hasBug(arr);
        System.out.println(expected == result? "TEST PASSED" : "TEST FAILED");
    }

    @ParameterizedTest
    @DisplayName("Задача 8")
    @CsvSource(value = {
            "2, 5, 2 4",
            "1,  10, 2 4 6 8 10"
    })
    void getEvenInRange(int start , int end, String expected) {
        String result = BaseJava.getEvenInRange(start,end);
        System.out.println(expected.equals(result) ? "TEST PASSED" : "TEST FAILED");
    }

    @RepeatedTest(10)
    @DisplayName("Задача 9")
    void findMax() {
        Random random = new Random();
        int[] myArray = random.ints(3, 1, 101).toArray();
        IntStream streamArray = IntStream.of(myArray);
        System.out.println(streamArray.max().getAsInt() == BaseJava.findMax(myArray) ? "TEST PASSED" : "TEST FAILED");
    }

    @Test
    @DisplayName("Задача 10")
    void reverse() {
        Random random = new Random();
        String[] words = {"One", "Two", "Three", "Four", "Five"};
        String[] input = new String[5];
        for (int i = 0; i < input.length; i++) {
            input[i] = words[random.nextInt(words.length)];
        }
        String[] expected = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            expected[i] = input[input.length - 1 - i];
        }
        System.out.println(Arrays.equals(expected,  BaseJava.reverse(input)) ? "TEST PASSED" : "TEST FAILED");
    }

    @Test
    @DisplayName("Задача 11")
    void calcAverage() {
        Random random = new Random();
        List<Integer> numbers = random.ints(10, 1, 101)  // 10 чисел от 1 до 100
                .boxed()
                .collect(Collectors.toList());
        double average = numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        System.out.println(average == BaseJava.calcAverage(numbers) ? "TEST PASSED" : "TEST FAILED");

    }

    @Test
    @DisplayName("Задача 12")
    void removeSpecificName() {
        String removeName = "Bob";
        List<String> namesList = Arrays.asList("Alice", "Bob", "Charlie", "Bob", "Diana");
        List<String> result = BaseJava.removeSpecificName(namesList, "Bob");
        List<String> expected = new ArrayList<>();
        for (String name : namesList) {
            if (removeName.equals(name)) {
                continue;
            }
            expected.add(name);
        }

        System.out.println(namesList.equals(result) ? "TEST PASSED" : "TEST FAILED");
    }
}
