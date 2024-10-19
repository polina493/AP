package cafe;
import java.util.concurrent.atomic.AtomicBoolean; // (коли кілька потоків потребують перевірки та зміни логічного значення)

// Годинник, для відкривання та закривання кав'ярні
class TimeThread extends Thread {
    private AtomicBoolean isOpen;  // кав'ярня відкрита

    public TimeThread(AtomicBoolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public void run() {
        try {
            System.out.println("Кав'ярня відкрита.");
            isOpen.set(true);  // кав'ярня відкрита
            Thread.sleep(10000);  // Час роботи кав'ярні
            System.out.println("Кав'ярня закривається.");
            isOpen.set(false);  // кав'ярня закрита
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
