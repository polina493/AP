package counts;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main{
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String text = "yehttps://github.com/polina493/AP.gits i love ice cream, and i love my mom, yes, yes ice cream pick me showel";
        text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        String[] words = text.split(" ");
        System.out.println(text);
        ConcurrentHashMap<String, AtomicInteger> wordCounts = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Callable<Void>> tasks = new ArrayList<>();

        int partSize = words.length / 4;
        for (int i = 0; i < 4; i++) {
            final int start = i * partSize;
            final int end = (i == 3) ? words.length : (i + 1) * partSize;

            tasks.add(() -> {
                for (int j = start; j < end; j++) {
                    wordCounts.computeIfAbsent(words[j].toLowerCase(), k -> new AtomicInteger(0)).incrementAndGet();
                }
                return null;
            });
        }

        List<Future<Void>> futures = executor.invokeAll(tasks);

        for (Future<Void> future : futures) {
            while (!future.isDone()) {
                System.out.println("Waiting ");
            }
        }

        executor.shutdown();

        System.out.println("Word Count Results:");
        wordCounts.forEach((word, count) -> System.out.println(word + ": " + count));
    }
}