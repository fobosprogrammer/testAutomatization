package ru.t1.authomatization.homework1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class BaseJavaTest {

    @Test
    @DisplayName("Задача 1")
    void isEven() {
        assertTrue(BaseJava.isEven(4),"Число нечетное");
        assertFalse(BaseJava.isEven(3),"Число четное");
        assertTrue(BaseJava.isEven(0),"Число нечетное");
        assertTrue(BaseJava.isEven(-2),"Число нечетное");
        assertFalse(BaseJava.isEven(-1),"Число четное");
    }

    @RepeatedTest(10)
    @DisplayName("Задача 2")
    void checkAccess() {
        Random random = new Random();

        int numberDenied = random.nextInt(18);
        assertEquals("Denied", BaseJava.checkAccess(numberDenied),"Доступ разрешен");
        int numberAllowed = random.nextInt(81) + 19;
        assertEquals("Allowed", BaseJava.checkAccess(numberAllowed),"Доступ не разрешен");
    }

    @RepeatedTest(10)
    @DisplayName("Задача 3")
    void isPositive() {
        Random random = new Random();
        int number = random.nextInt(199) - 99;
        assertEquals(BaseJava.isPositive(number),number > 0,"Число не соответствует ожиданию");
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
        assertEquals(expectedGrade, result, "Оценка для балла " + score + " должна быть " + expectedGrade);
    }

    @DisplayName("Задача 5")
    @ParameterizedTest
    @CsvSource({
            "5, 5 4 3 2 1 Поехали!",
            "4, 4 3 2 1 Поехали!"
    })
    void blastOff(int number,String expectedString) {
        String result = BaseJava.blastOff(number);
        assertEquals(expectedString, result, "Строка " + result + " должна быть " + expectedString);
    }

    @RepeatedTest(10)
    @DisplayName("Задача 6")
    void sumToN() {
        Random random = new Random();
        int number = random.nextInt(20);
        assertEquals(number * (number + 1) / 2, BaseJava.sumToN(number), "Сумма чисел от 1 до " + number + " должна быть " + number * (number + 1) / 2);;
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
        assertEquals(expected, result, "Ожидаемый результат для массива: " + java.util.Arrays.toString(arr));
    }

    @ParameterizedTest
    @DisplayName("Задача 8")
    @CsvSource(value = {
            "2, 5, 2 4",
            "1,  10, 2 4 6 8 10"
    })
    void getEvenInRange(int start , int end, String expected) {
        String result = BaseJava.getEvenInRange(start,end);
        assertEquals(expected,result, "Ожидаемый результат - " + expected + " не соответствует - " + result);
    }

    @RepeatedTest(10)
    @DisplayName("Задача 9")
    void findMax() {
        Random random = new Random();
        int[] myArray = random.ints(3, 1, 101).toArray();
        IntStream streamArray = IntStream.of(myArray);
        assertEquals(streamArray.max().getAsInt(),  BaseJava.findMax(myArray), "Максимальное значение в массиве должно быть правильно определено");

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
        assertArrayEquals(expected, BaseJava.reverse(input));

        // Тест с одним элементом
        String[] input2 = {"Single"};
        String[] expected2 = {"Single"};
        assertArrayEquals(expected2, BaseJava.reverse(input2));

        // Тест с пустым массивом
        String[] input3 = {};
        String[] expected3 = {};
        assertArrayEquals(expected3, BaseJava.reverse(input3));

        // Тест с null
        assertNull(BaseJava.reverse(null));
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
        assertEquals(average, BaseJava.calcAverage(numbers), "Среднее арифметическое рассчитано неправильно");
    }

    @Test
    @DisplayName("Задача 12")
    void removeSpecificName() {
        List<String> namesList = Arrays.asList("Alice", "Bob", "Charlie", "Bob", "Diana");
        List<String> result = BaseJava.removeSpecificName(namesList, "Bob");

        assertEquals(Arrays.asList("Alice", "Charlie", "Diana"), result);
        assertTrue(result.contains("Alice"));
        assertFalse(result.contains("Bob"));
        assertEquals(3, result.size());
    }
}