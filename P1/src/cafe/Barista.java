package cafe;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

// Клас Barista представляє баристу, який готує каву.
// Бариста — це також окремий потік, який обробляє замовлення клієнтів.
class Barista implements Runnable {
    private Semaphore semaphore;  // Семафор для синхронізації роботи з клієнтами
    private AtomicBoolean isOpen; // Кав'ярня відкрита чи закрита

    public Barista(Semaphore semaphore, AtomicBoolean isOpen) {
        this.semaphore = semaphore;  // Призначаємо семафор для баристи
        this.isOpen = isOpen;
    }

    @Override
    public void run() {
        try {
            while (isOpen.get() || semaphore.availablePermits() < 2) {
                System.out.println("Бариста працює...");
                Thread.sleep(3000);
            }
            System.out.println("Бариста йде додому.");
        } catch (InterruptedException e) {
            e.printStackTrace();  // Обробка можливих помилок (переривання потоку)
        }
    }
}
