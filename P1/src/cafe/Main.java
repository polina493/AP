package cafe;
import java.util.concurrent.Semaphore;

// Створення потоків і використання Thread states, використання інтерфейсу Runnable.
// Обробити помилки, що можуть виникати під час виконання.
// Варіант 5
// Кав'ярня
// Створіть програму, де кілька клієнтів (потоки) приходять до кав'ярні та роблять замовлення на каву.
// Один потік (бариста) відповідає за приготування кави.
// Використовуйте семафори, щоб обмежити кількість замовлень, що одночасно готуються
// (наприклад, 2 замовлення одночасно).
// Враховуйте робочі години: коли кав’ярня зачинена, ніхто не може увійти, лише працівник піти додому.
public class Main {
    public static void main(String[] args) {
        // Створюємо семафор з двома дозволами (максимум два замовлення одночасно)
        Semaphore semaphore = new Semaphore(2);

        // Створюємо клієнтів (кожен клієнт — це окремий потік)
        Thread customer1 = new Thread(new Customer("Клієнт 1", semaphore));
        Thread customer2 = new Thread(new Customer("Клієнт 2", semaphore));
        Thread customer3 = new Thread(new Customer("Клієнт 3", semaphore));

        // Створюємо баристу, як окремий потік
        Thread barista = new Thread(new Barista(semaphore));

        // Запускаємо потоки
        barista.start();
        customer1.start();
        customer2.start();
        customer3.start();
    }
}
