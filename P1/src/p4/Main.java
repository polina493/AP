package p4;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Ініціалізація програми
        System.out.println("Початок виконання програми.");

        // Асинхронно генеруємо масив із 10 випадкових цілих чисел
        CompletableFuture<int[]> generateArrayFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            // Генерування 10 випадкових чисел в діапазоні від 1 до 100
            int[] array = new Random().ints(10, 1, 100).toArray();
            long end = System.currentTimeMillis();
            System.out.println("Масив згенеровано за " + (end - start) + " мс.");
            // Вивід масиву
            System.out.println("Поточний масив: " + Arrays.toString(array));
            return array;
        });

        // Додаємо 10 до кожного елемента асинхронно
        CompletableFuture<double[]> addTenFuture = generateArrayFuture.thenApplyAsync(array -> {
            long start = System.currentTimeMillis();
            System.out.println("Розпочато додавання 10 до кожного елемента.");
            int[] updatedArray = Arrays.stream(array).map(i -> i + 10).toArray();
            long end = System.currentTimeMillis();
            System.out.println("Додано 10 до кожного елемента за " + (end - start) + " мс.");
            // Вивід зміненого масиву
            System.out.println("Поточний масив після додавання: " + Arrays.toString(updatedArray));
            return Arrays.stream(updatedArray).asDoubleStream().toArray();
        });

        // Ділимо кожен елемент на 2 асинхронно
        CompletableFuture<double[]> divideByTwoFuture = addTenFuture.thenApplyAsync(array -> {
            long start = System.currentTimeMillis();
            System.out.println("Розпочато ділення кожного елемента на 2.");
            // Дялення коженого елемента на 2
            double[] dividedArray = Arrays.stream(array).map(i -> i / 2.0).toArray();
            long end = System.currentTimeMillis();
            System.out.println("Поділено кожен елемент на 2 за " + (end - start) + " мс.");
            // Вивід зміненого масиву
            System.out.println("Поточний масив після ділення: " + Arrays.toString(dividedArray));
            return dividedArray;
        });

        // Виводимо фінальний масив із повідомленням асинхронно
        CompletableFuture<Void> printResultFuture = divideByTwoFuture.thenAcceptAsync(array -> {
            long start = System.currentTimeMillis();
            System.out.println("Розпочато виведення кінцевого масиву.");
            System.out.println("Кінцевий масив: " + Arrays.toString(array));
            long end = System.currentTimeMillis();
            System.out.println("Результат виведено за " + (end - start) + " мс.");
        });

        printResultFuture.thenRunAsync(() -> {
            long endTime = System.currentTimeMillis();
            // Вивід загального часу виконання
            System.out.println("Усі завдання завершено за " + (endTime - startTime) + " мс.");
        });

        // Очікуємо завершення завдань перед виходом із програми
        try {
            System.out.println("Очікування завершення завдань...");
            // Чекаємо, завершення усих асинхронних завданнь
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            System.out.println("Сталася помилка під час очікування завершення завдань.");
            e.printStackTrace();
        }

        System.out.println("Програма завершила виконання.");
    }
}
