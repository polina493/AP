package balance;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Отримання параметрів від користувача
        System.out.println("Enter number of rows:");
        int rows = getPositiveIntInput(scanner);

        System.out.println("Enter number of columns:");
        int cols = getPositiveIntInput(scanner);

        System.out.println("Enter minimum value of array elements:");
        int minValue = scanner.nextInt();

        System.out.println("Enter maximum value of array elements:");
        int maxValue = scanner.nextInt();

        if (minValue > maxValue) {
            System.out.println("Minimum value cannot be greater than maximum value. Exiting...");
            return;
        }

        int[][] array = generateRandomArray(rows, cols, minValue, maxValue);

        // Вивід згенерованого масиву
        System.out.println("Generated Array:");
        printArray(array);

        // Пошук через Fork/Join з Work Stealing
        ForkJoinPool pool = new ForkJoinPool();
        long startStealing = System.currentTimeMillis();
        Integer stealingResult = pool.invoke(new WorkStealingTask(array, 0, rows));
        long endStealing = System.currentTimeMillis();
        System.out.println("Work Stealing Result: " + (stealingResult != null ? stealingResult : "No match found"));
        System.out.println("Work Stealing Time: " + (endStealing - startStealing) + "ms");

        // Пошук через ExecutorService з Work Dealing
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        long startDealing = System.currentTimeMillis();
        WorkDealingTask dealingTask = new WorkDealingTask(array, 0, rows, executorService);
        Integer dealingResult = dealingTask.compute();
        long endDealing = System.currentTimeMillis();
        executorService.shutdown();
        System.out.println("Work Dealing Result: " + (dealingResult != null ? dealingResult : "No match found"));
        System.out.println("Work Dealing Time: " + (endDealing - startDealing) + "ms");
    }

    // Метод для генерації двовимірного масиву з рандомними значеннями
    private static int[][] generateRandomArray(int rows, int cols, int minValue, int maxValue) {
        Random random = new Random();
        int[][] array = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = random.nextInt(maxValue - minValue + 1) + minValue;
            }
        }
        return array;
    }

    // Вивід масиву у консоль
    private static void printArray(int[][] array) {
        for (int[] row : array) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    // Перевірка на коректне позитивне число
    private static int getPositiveIntInput(Scanner scanner) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.next());
                if (input > 0) {
                    return input;
                } else {
                    System.out.println("Please enter a positive number:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive number:");
            }
        }
    }

    // Задача для Fork/Join Framework з Work Stealing
    static class WorkStealingTask extends RecursiveTask<Integer> {
        private final int[][] array;
        private final int startRow;
        private final int endRow;

        public WorkStealingTask(int[][] array, int startRow, int endRow) {
            this.array = array;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        protected Integer compute() {
            // Якщо кількість рядків маленька, обробляємо завдання без розбиття
            if (endRow - startRow <= 10) {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < array[i].length; j++) {
                        // Перевірка, чи елемент дорівнює сумі індексів
                        if (array[i][j] == i + j) {
                            return array[i][j];
                        }
                    }
                }
                return null;
            } else {
                // Розбиття задачі на дві підзадачі
                int mid = (startRow + endRow) / 2;
                WorkStealingTask task1 = new WorkStealingTask(array, startRow, mid);
                WorkStealingTask task2 = new WorkStealingTask(array, mid, endRow);
                task1.fork(); // Асинхронне виконання першої підзадачі
                Integer result = task2.compute(); // Виконання другої підзадачі
                if (result != null) return result; // Якщо знайдено результат, повертаємо його
                return task1.join(); // Завершення першої підзадачі та повернення результату
            }
        }
    }

    // Задача для ExecutorService з Work Dealing
    static class WorkDealingTask {
        private final int[][] array;
        private final int startRow;
        private final int endRow;
        private final ExecutorService executorService;

        public WorkDealingTask(int[][] array, int startRow, int endRow, ExecutorService executorService) {
            this.array = array;
            this.startRow = startRow;
            this.endRow = endRow;
            this.executorService = executorService;
        }

        public Integer compute() {
            // Розподіл задач між потоками у пулі
            for (int i = startRow; i < endRow; i++) {
                int row = i; // Локальна змінна для лямбда-виразу
                executorService.submit(() -> {
                    for (int j = 0; j < array[row].length; j++) {
                        // Перевірка елемента на відповідність умові
                        if (array[row][j] == row + j) {
                            System.out.println("Found element in row " + row);
                            return array[row][j]; // Повернення знайденого значення
                        }
                    }
                    return null; // Повернення null, якщо елемент не знайдено
                });
            }
            return null; // Загальний випадок, якщо результат не знайдено
        }
    }
}
