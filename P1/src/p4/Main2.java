package p4;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main2 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Генеруємо послідовність випадкових чисел
        CompletableFuture<double[]> generateSequenceFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            double[] sequence = new Random().doubles(20, 1.0, 100.0).toArray();
            long end = System.currentTimeMillis();
            System.out.println("Генерація послідовності завершена за " + (end - start) + " мс.");
            System.out.println("Початкова послідовність: " + Arrays.toString(Arrays.stream(sequence).mapToObj(num -> String.format("%.2f", num)).toArray()));
            return sequence;
        });

        // Обчислюємо результат
        CompletableFuture<Double> calculateProductFuture = generateSequenceFuture.thenApplyAsync(sequence -> {
            long start = System.currentTimeMillis();
            double product = 1.0;
            for (int i = 1; i < sequence.length; i++) {
                product *= (sequence[i] - sequence[i - 1]);
            }
            long end = System.currentTimeMillis();
            System.out.println("Обчислення результату завершено за " + (end - start) + " мс.");
            return product;
        });

        // Виводимо результат
        CompletableFuture<Void> printResultFuture = calculateProductFuture.thenAcceptAsync(product -> {
            long start = System.currentTimeMillis();
            System.out.println("Результат обчислень: " + String.format("%.2f", product));
            long end = System.currentTimeMillis();
            System.out.println("Результат виведено за " + (end - start) + " мс.");
        });

        // Виводимо час виконання усіх завдань
        printResultFuture.thenRunAsync(() -> {
            long endTime = System.currentTimeMillis();
            System.out.println("Час виконання всіх асинхронних операцій: " + (endTime - startTime) + " мс.");
        });

        // Очікуємо завершення асинхронних завдань перед виходом із програми
        try {
            System.out.println("Очікування завершення всіх операцій...");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Сталася помилка під час очікування завершення.");
            e.printStackTrace();
        }

        System.out.println("Програма завершила виконання.");
    }
}
