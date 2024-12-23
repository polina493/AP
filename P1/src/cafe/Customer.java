package cafe;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

// Клас Customer представляє клієнта, який робить замовлення на каву.
// Кожен клієнт є окремим потоком, який взаємодіє з кав'ярнею через семафор.
class Customer implements Runnable {
    private String name;  // Ім'я клієнта
    private Semaphore semaphore;  // Семафор для обмеження кількості одночасних замовлень
    private AtomicBoolean isOpen; // Кав'ярня відкрита чи закрита

    public Customer(String name, Semaphore semaphore, AtomicBoolean isOpen) {
        this.name = name;
        this.semaphore = semaphore;  // Призначаємо семафор для клієнта
        this.isOpen = isOpen;
    }

    @Override
    public void run() {
        try {
            // Перевіряємо чи кав'ярня відчинена, перед замовленням
            if (!isOpen.get()) {
                System.out.println(name + " не може зробити замовлення, кав'ярня зачинена.");
                return;
            }
            // Клієнт хоче зробити замовлення, але чекає, поки звільниться місце для замовлення
            System.out.println(name + " хоче зробити замовлення.");

            // Семафор блокує потік, якщо всі дозволи (два) зайняті
            semaphore.acquire();

            if (!isOpen.get()) {
                System.out.println(name + " не може зробити замовлення, кав'ярня зачинена.");
                return;
            }

            // Якщо семафор дав дозвіл, клієнт робить замовлення
            System.out.println(name + " робить замовлення.");

            // Імітація часу на приготування замовлення (2 секунди)
            Thread.sleep(2000);

            // Клієнт отримав свою каву
            System.out.println(name + " отримав каву.");
        } catch (InterruptedException e) {
            e.printStackTrace();  // Обробка можливих помилок (переривання потоку)
        } finally {
            // Звільняємо місце для наступного клієнта, коли замовлення виконано
            semaphore.release();
        }
    }
}