package arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Запитуємо у користувача діапазон чисел
        System.out.print("Введіть мінімальне значення діапазону: ");
        int min = scanner.nextInt();

        System.out.print("Введіть максимальне значення діапазону: ");
        int max = scanner.nextInt();

        int size = 50; // Розмір масиву між 40 та 60
        int chunkSize = 10; // Для розбиття масиву на частини для обробки у окремих потоках

        // Ініціалізуємо і заповнюємо масив випадковими числами в діапазоні [min, max]
        int[] array = new Random().ints(size, min, max + 1).toArray();

        // Створюємо CopyOnWriteArraySet для безпечного зберігання результатів від різних потоків
        CopyOnWriteArraySet<Integer> results = new CopyOnWriteArraySet<>();

        // Створюємо ExecutorService для управління потоками
        ExecutorService executor = Executors.newFixedThreadPool(size / chunkSize);

        // Список для збереження об'єктів Future
        List<Future<List<Integer>>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis(); // Починаємо вимірювання часу виконання

        // Розбиваємо масив на частини і обробляємо кожну частину асинхронно
        for (int i = 0; i < size; i += chunkSize) {
            int[] chunk = java.util.Arrays.copyOfRange(array, i, Math.min(size, i + chunkSize));
            Callable<List<Integer>> task = new PairwiseProductTask(chunk);
            Future<List<Integer>> future = executor.submit(task);
            futures.add(future);
        }

        // Збираємо результати з кожного Future
        futures.forEach(future -> {
            try {
                if (future.isDone() && !future.isCancelled()) {
                    results.addAll(future.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        // Завершуємо роботу ExecutorService
        executor.shutdown();

        // Виводимо результати
        System.out.println("Попарні добутки: " + results);

        // Обчислюємо і виводимо час виконання
        long endTime = System.currentTimeMillis();
        System.out.println("Час виконання: " + (endTime - startTime) + "мс");
    }
}

// Клас Callable для обчислення попарних добутків
class PairwiseProductTask implements Callable<List<Integer>> {
    private final int[] array;

    public PairwiseProductTask(int[] array) {
        this.array = array;
    }

    @Override
    public List<Integer> call() {
        List<Integer> products = new ArrayList<>();
        for (int i = 0; i < array.length - 1; i += 2) {
            products.add(array[i] * array[i + 1]);
        }
        return products;
    }
}
