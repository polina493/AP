# AP 
### Практичне завдання 2
Проєкт демонструє багатопоточну обробку масивів чисел із використанням `Callable` та `Future`. Користувач задає діапазон чисел, які обробляються в окремих потоках, з підрахунком часу виконання.

### Practice 3 task 1
Програма реалізує пошук елемента у двовимірному масиві, значення якого дорівнює сумі його індексів, двома підходами:
Work Stealing (Fork/Join Framework).
Work Dealing (ExecutorService).
Результати:
Work Stealing: 2 ms (ефективний через динамічний розподіл задач).
Work Dealing: 18 ms (додаткові витрати на синхронізацію через Future).
Висновок:
Work Stealing значно швидший, особливо для задач із нерівномірним навантаженням.
### Practice 3 task 2
Work Dealing був обраний для цієї задачі, бо легше реалізувати в порівнянні з Work Stealing, де потрібно буде вручну контролювати розподіл задач між потоками.







 
 
