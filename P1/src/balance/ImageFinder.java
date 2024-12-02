//        2. Напишіть програму, що буде вирішувати завдання відповідно до
//        варіанту. У програмі доцільно використовуйте Executor Service, Thread Pool і
//        Fork/Join Framework.
//        Для реалізації рішення потрібно завдання поділяти на підзадачі, які
//        виконуватимуться асинхронно. Реалізуйте виконання завдання, використовуючи
//        підхід Work stealing або Work dealing.
//        Для вирішення завдань відповідно за варіантами заготуйте підходящі
//        директорії та файли.
//Варіант 5
//        2. Напишіть програму, яка буде проходити по файлам певної
//        директорії та знаходити серед них усі зображення.
//        Директорію має обирати користувач.
//        У результаті потрібно вивести кількість знайдених файлів і
//        відкрити останній.

package balance;

import java.io.File;
import java.awt.Desktop;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.Scanner;

public class ImageFinder {
    private static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Отримуємо директорію від користувача
        System.out.println("Enter directory path:");
        String directoryPath = scanner.nextLine();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory. Exiting...");
            return;
        }

        // Запуск пошуку зображень у директорії
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<File> imageFiles = findImages(directory, executorService);
        long endTime = System.currentTimeMillis();
        executorService.shutdown();

        // Виведення результатів
        System.out.println("Found " + imageFiles.size() + " image(s).");
        if (!imageFiles.isEmpty()) {
            File lastImage = imageFiles.get(imageFiles.size() - 1);
            System.out.println("Opening last image: " + lastImage.getAbsolutePath());

            // Спроба відкрити останнє зображення за допомогою Desktop
            try {
                Desktop.getDesktop().open(lastImage);
            } catch (Exception e) {
                System.out.println("Failed to open the image.");
                e.printStackTrace();
            }
        }
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }

    // Метод для пошуку зображень в директорії
    private static List<File> findImages(File directory, ExecutorService executorService) {
        List<File> imageFiles = new ArrayList<>();
        List<Future<List<File>>> futures = new ArrayList<>();

        // Додаємо завдання для кожного підкаталогу
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                futures.add(executorService.submit(() -> findImagesInDirectory(file)));
            } else if (isImage(file)) {
                imageFiles.add(file);
            }
        }

        // Чекаємо завершення всіх завдань
        for (Future<List<File>> future : futures) {
            try {
                imageFiles.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return imageFiles;
    }

    // Метод для пошуку зображень у конкретній директорії
    private static List<File> findImagesInDirectory(File directory) {
        List<File> imageFiles = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                // Рекурсивно перевіряємо підкаталоги
                imageFiles.addAll(findImagesInDirectory(file));
            } else if (isImage(file)) {
                imageFiles.add(file);
            }
        }
        return imageFiles;
    }

    // Перевірка, чи є файл зображенням
    private static boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        for (String extension : IMAGE_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
