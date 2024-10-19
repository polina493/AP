package cafe;
import java.util.concurrent.Semaphore;

// Клас Barista представляє баристу, який готує каву.
// Бариста — це також окремий потік, який обробляє замовлення клієнтів.
class Barista implements Runnable {
    private Semaphore semaphore;  // Семафор для синхронізації роботи з клієнтами

    public Barista(Semaphore semaphore) {
        this.semaphore = semaphore;  // Призначаємо семафор для баристи
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Бариста готовий до приготування замовлень.");

                // Імітація очікування на нові замовлення (5 секунд)
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  // Обробка можливих помилок (переривання потоку)
        }
    }
}
