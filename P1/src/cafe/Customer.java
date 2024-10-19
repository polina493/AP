package cafe;
import java.util.concurrent.Semaphore;

// Клас Customer представляє клієнта, який робить замовлення на каву.
// Кожен клієнт є окремим потоком, який взаємодіє з кав'ярнею через семафор.
class Customer implements Runnable {
    private String name;  // Ім'я клієнта
    private Semaphore semaphore;  // Семафор для обмеження кількості одночасних замовлень

    public Customer(String name, Semaphore semaphore) {
        this.name = name;
        this.semaphore = semaphore;  // Призначаємо семафор для клієнта
    }

    @Override
    public void run() {
        try {
            // Клієнт хоче зробити замовлення, але чекає, поки звільниться місце для замовлення
            System.out.println(name + " хоче зробити замовлення.");

            // Семафор блокує потік, якщо всі дозволи (два) зайняті
            semaphore.acquire();

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