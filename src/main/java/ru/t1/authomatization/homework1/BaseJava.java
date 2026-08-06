package ru.t1.authomatization.homework1;

import java.util.ArrayList;
import java.util.List;

public class BaseJava {
    /**
     * Метод возвращает true, если число чётное, и false — если нечётное.
     */
    public static boolean isEven(int n) {
        return (n % 2 == 0);
    }

    /**
     * Метод проверяет возраст, если age строго больше 18 возвращает Allowed, и Denied — если меньше.
     */
    public static String checkAccess(int age) {
        if (age > 18) {
            return "Allowed";
        }
        return "Denied";
    }

    /**
     * Возвращает true если n Больше или равно 0
     */
    public static boolean isPositive(int n) {
        return n >= 0 ? true : false;
    }

    /*
     * Метод возвращает строку, соответствующую строгому вхождению в границы:
     *    0–20: E;
     *   21–40: D;
     *   41–60: C;
     *   61–80: B;
     *   81–100: A.
     * Если переданное число не входит в границы — вернуть строку Error.
     */
    public static String getGrade(int score) {
        if (score >= 0 && score <= 20) {
            return "E";
        }
        if (score >= 21 && score <= 40) {
            return "D";
        }
        if (score >= 41 && score <= 60) {
            return "C";
        }
        if (score >= 61 && score <= 80) {
            return "B";
        }
        if (score >= 81 && score <= 100) {
            return "A";
        }

        return "Error";
    }

    /**
     * Метод принимает стартовое число (например, 5) и возвращает строку со всеми числами до 1 и словом «Поехали!» в конце (например, «5 4 3 2 1 Поехали!»).
     * @param start
     * @return
     */
    public static String blastOff(int start) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            result.append(i).append(" ");
        }
        result.append("Поехали!");
        return result.toString();
    }

    /**
     * Метод возвращает сумму всех целых чисел от 1 до n.
     * @param n
     * @return
     */
    public static int sumToN(int n) {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            result += i;
        }
        return result;
    }

    /**
     * Метод принимает массив строк и возвращает true, если хотя бы одна строка в массиве равна Bug. Сравнение можно выполнять без учёта регистра.
     * @param messages
     * @return
     */
    public static boolean hasBug(String[] messages) {
        if(messages == null)
            return false;
        for (String message : messages) {
            if (message != null && "bug".equalsIgnoreCase(message)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод принимает границы диапазона и возвращает строку, состоящую только из чётных чисел внутри этого промежутка (включая границы),
     * разделённых пробелом. Перед первым и после последнего числа пробел не ставится. Например: (2, 5) -> “2 4”
     * @param start
     * @param end
     */
    public static String getEvenInRange(int start, int end) {
        if(end < start ) {
            throw new IllegalArgumentException("End должен быть больше start");
        }
        StringBuilder result = new StringBuilder();

        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                result.append(i).append(" ");
            }
        }

        return result.toString().trim();
    }

    /**
     * Метод находит и возвращает самое большое число в переданном массиве
     * @param arr
     * @return
     */
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Массив не может быть null или пустым");
        }

        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     *  Метод возвращает новый массив, в котором элементы исходного массива расположены в обратном порядке. Например, {“One”, “Two”, “Zero”} -> {“Zero”, “Two”, “One}.
     * @param arr
     * @return
     */
    public static String[] reverse(String[] arr) {
        if (arr == null) {
            return null;
        }

        String[] reversed = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversed[i] = arr[arr.length - 1 - i];
        }
        return reversed;
    }

    /**
     *  Метод вычисляет и возвращает среднее арифметическое всех чисел в списке.
     * @param list
     */
    public static Double calcAverage(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Массив не может быть null или пустым");
        }

        return list.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Метод принимает список и имя, которое нужно исключить. Возвращает новый список, не содержащий указанного имени.
     * @param list
     * @param nameToRemove
     * @return
     */
    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        if (list == null) {
            return new ArrayList<>();
        }

        return list.stream()
                .filter(name -> !name.equals(nameToRemove))
                .toList();
    }
}
