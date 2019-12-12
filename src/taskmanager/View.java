package taskmanager;

import taskmanager.conrollers.Manager;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.Task;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Пользовательский интерфейс, выводящий пользователю информацию о работе с журналом задач
 */
public class View implements TaskChangedSubscriber {
    /**
     * Сканер ввода
     */
    private Scanner input = new Scanner(System.in);
    /**
     * Журнал задач
     */
    private Manager journalTask; //д.б. манагер
    /**
     * Маркер выхода из программы
     */
    private boolean exit = false;

    enum MenuItem {
        EXIT,
        ADD_TASK,
        EDIT_TASK,
        DELETE_TASK,
        VIEW_JOURNAL
    }

    enum EditMenuItem {
        EXIT,
        EDIT_NAME,
        EDIT_DESCRIPTION,
        EDIT_TIME,
        ANOTHER_TASK
    }

    public View(Manager journalTask) {
        this.journalTask = journalTask;
    }

    /**
     * Запуск пользовательского интерфейса
     */
    public void start() {
        while (!exit) {
            consoleClear(); // "очистка" консоли
            System.out.println("[TASK MANAGER]\n");
            menu();
            System.out.println("\nВыберите пункт меню:");
            boolean key = false;
            try {
                int menuChoice = input.nextInt();
                if (menuChoice >= 0 && menuChoice <= 4) {
                    for (MenuItem item : MenuItem.values()) {
                        if (menuChoice == item.ordinal()) {
                            action(item);
                        }
                    }
                } else {
                    showMessage("incorrectInput");
                }
            } catch (InputMismatchException ex) {
                showMessage("incorrectInput");
            }
        }
        System.out.println("Завершение работы программы...");
    }

    /**
     * Вывод главного меню
     */
    private void menu() {
        System.out.println("Меню:" +
                "\n[1] Добавить задачу" +
                "\n[2] Редактировать задачу" +
                "\n[3] Удалить задачу" +
                "\n[4] Список задач" +
                "\n[0] Выход");
    }

    /**
     * Обработка действия пользователя
     *
     * @param item Пользовательский выбор пункта меню
     */
    private void action(MenuItem item) {
        boolean key = false;
        switch (item) {
            case ADD_TASK: { //добавить задачу
                consoleClear();
                System.out.println("[Добавление задачи]");
                String name = null;
                int nameInputCount = 0; //счетчик запусков ввода названия
                while (!key) { //появляется лишний энтер при повторном входе в блок трай
                    try {
                        if (nameInputCount == 0) {
                            System.out.println("\nНазвание: ");
                            input.nextLine(); //отлавливаем энтер
                            nameInputCount++;
                        } else {
                            System.out.println("\nНазвание: ");
                        }
                        name = input.nextLine();
                        journalTask.checkUniqueName(name);
                        key = true;
                    } catch (NameTaskException ex) {
                        System.out.println(ex.getMessage() + " Повторите ввод.");
                    }
                }
                key = false;
                System.out.print("\nОписание:\n");
                String description = input.nextLine();
                LocalTime time = LocalTime.of(0, 0, 0);
                while (!key) {
                    System.out.print("\nВремя (ЧЧ:ММ): ");
                    String strTime = input.nextLine();
                    StringParser parser = new StringParser();
                    try {
                        time = parser.timeParse(strTime);
                        key = true;
                    } catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException ex) {
                        System.out.println("Некорректное значение времени. Повторите ввод.");
                    }
                }
                Task task = new Task(name, description, time);
                try {
                    journalTask.addItem(task);
                } catch (NameTaskException ex) {
                    System.out.println(ex.getMessage() + ". Повторите ввод.");
                }
                System.out.println("\nНажмите Enter, чтобы продолжить...");
                input.nextLine();
                break;
            }

            case EDIT_TASK: {//редактировать задачу
                if (journalTask.size() == 0) {
                    showMessage("emptyList");
                } else {
                    boolean isEditCorrect = false;
                    while (!isEditCorrect) {
                        consoleClear();
                        System.out.println("[Редактирование задачи]\n");
                        showTaskList();
                        System.out.println("\nИндекс: ");
                        Task task = null;
                        int index = 0;
                        while (!key) {
                            input.nextLine();
                            try {
                                index = input.nextInt();
                                index--;
                                task = journalTask.getItem(index);
                                key = true;
                            } catch (InputMismatchException | ItemNotFoundException ex) {
                                System.out.println("Неверное значение индекса. Повторите ввод.");
                            }
                        }
                        key = false;
                        boolean isMenuClose = false;
                        while (!isMenuClose) {
                            consoleClear();
                            System.out.println("\nРедактируемая задача:");
                            System.out.println(task.toString());
                            System.out.println("\nМеню редактирования:" +
                                    "\n[1] Название" +
                                    "\n[2] Описание" +
                                    "\n[3] Время" +
                                    "\n[4] Выбрать другую задачу" +
                                    "\n[0] Выйти в главное меню" +
                                    "\n\nВыберите пункт меню: ");
                            int editChoice = 0;
                            while (!key) {
                                try {
                                    editChoice = input.nextInt();
                                    if (editChoice >= 0 && editChoice <= 4) key = true;
                                    else System.out.println("Некорректное значение. Повторите ввод");
                                } catch (InputMismatchException ex) {
                                    System.out.println("Некорректное значение. Повторите ввод");
                                    input.nextLine();
                                }
                            }
                            key = false;
                            EditMenuItem editItem = null;
                            for (EditMenuItem editmenuItem : EditMenuItem.values()) {
                                if (editChoice == editmenuItem.ordinal()) {
                                    editItem = editmenuItem;
                                }
                            }
                            switch (editItem) {
                                case EDIT_NAME:
                                    String name = null;
                                    int nameInputCount = 0; //счетчик запусков ввода названия
                                    while (!key) { //появляется лишний энтер при повторном входе в блок трай
                                        try {
                                            if (nameInputCount == 0) {
                                                System.out.println("\nНовое название: ");
                                                input.nextLine(); //отлавливаем энтер
                                                nameInputCount++;
                                            } else {
                                                System.out.println("\nНовое название: ");
                                            }
                                            name = input.nextLine();
                                            journalTask.checkUniqueName(name);
                                            key = true;
                                        } catch (NameTaskException ex) {
                                            System.out.println(ex.getMessage() + " Повторите ввод.");
                                        }
                                    }
                                    key = false;
                                    try {
                                        journalTask.editTask(index, name);
                                    } catch (ItemNotFoundException ex) {
                                        System.out.println(ex.getMessage() + ". Повторите ввод.");
                                    }
                                    break;
                                case EDIT_DESCRIPTION:
                                    System.out.print("\nНовое описание: ");
                                    input.nextLine();
                                    String description = input.nextLine();
                                    try {
                                        journalTask.editTaskDescription(index, description);
                                    } catch (ItemNotFoundException ex) {
                                        System.out.println(ex.getMessage() + ". Повторите ввод.");
                                    }
                                    break;
                                case EDIT_TIME:
                                    LocalTime time = LocalTime.of(0, 0, 0);
                                    while (!key) {
                                        System.out.print("\nНовое время (ЧЧ:ММ): ");
                                        input.nextLine();
                                        String strTime = input.nextLine();
                                        StringParser parser = new StringParser();
                                        try {
                                            time = parser.timeParse(strTime);
                                            key = true;
                                        } catch (NumberFormatException | DateTimeException ex) {
                                            System.out.println("Некорректное значение времени. Повторите ввод.");
                                        }
                                    }
                                    key = false;
                                    try {
                                        journalTask.editTask(index, time);
                                    } catch (ItemNotFoundException ex) {
                                        System.out.println(ex.getMessage() + ". Повторите ввод.");
                                    }
                                    break;
                                case ANOTHER_TASK:
                                    isMenuClose = true;
                                    break;
                                case EXIT:
                                    isMenuClose = true;
                                    isEditCorrect = true;

                                    break;
                            }
                        }
                    }
                }
                break;
            }
            case DELETE_TASK: {//удалить задачу
                if (journalTask.size() == 0) {
                    showMessage("emptyList");
                } else {
                    consoleClear();
                    System.out.println("[Удаление задачи]\n\n");
                    showTaskList();
                    System.out.println("Индекс: ");
                    Task task = null;
                    int index = 0;
                    while (!key) {
                        try {
                            index = input.nextInt();
                            index--;
                            task = journalTask.getItem(index);
                            key = true;
                        } catch (InputMismatchException | ItemNotFoundException ex) {
                            System.out.println("Неверное значение индекса. Повторите ввод.");
                        }
                    }
                    key = false;
                    System.out.println("\nБудет удалена следующая задача:");
                    System.out.println(task.toString());
                    int deleteChoice = 0;
                    while (!key) {
                        System.out.println("\nВы уверены, что хотите удалить задачу?");
                        System.out.println("[1] Да          [2] Нет");
                        try {
                            deleteChoice = input.nextInt();
                            if (deleteChoice == 1 || deleteChoice == 2) key = true;
                            else System.out.println("Некорректное значение. Повторите ввод");
                        } catch (InputMismatchException ex) {
                            System.out.println("Некорректное значение. Повторите ввод");
                            input.nextLine();
                        }
                    }
                    key = false;
                    switch (deleteChoice) {
                        case 1:
                            try {
                                journalTask.deleteItem(index);
                            } catch (ItemNotFoundException ex) {
                                System.out.println(ex.getMessage() + " Повторите ввод.");
                            }
                            System.out.println("Нажмите Enter, чтобы продолжить...");
                            break;
                        case 2:
                            System.out.println("Задача не удалена. Нажмите Enter, чтобы продолжить...");
                            break;
                        default:
                            System.out.println("Некорректное значение. Повторите ввод.");
                            break;
                    }
                    input.nextLine(); //отлавливаем энтер
                    input.nextLine();
                }
                break;
            }
            case VIEW_JOURNAL: {//вывести список задач
                if (journalTask.size() == 0) {
                    showMessage("emptyList");
                } else {
                    consoleClear();
                    System.out.println("[Список задач]\n");
                    showTaskList();
                    System.out.println("\nНажмите Enter, чтобы вернуться в главное меню...");
                    input.nextLine(); //отлавливаем энтер
                    input.nextLine();
                }
                break;
            }
            case EXIT: //выход
                exit = true;
                journalTask.finalWork();
                break;
        }
    }

    /**
     * Вывод на экран списка задач
     */
    private void showTaskList() {
        if (journalTask.size() == 0) {
            System.out.println("Журнал задач пуст.\n");
            return;
        }
        try {
            for (int i = 0; i < journalTask.size(); i++) {
                if (i < 9) {
                    System.out.println(i + 1 + ".  " + journalTask.getItem(i).toString());
                } else {
                    System.out.println(i + 1 + ". " + journalTask.getItem(i).toString());
                }
                System.out.println("----------------------------------------------------------------------");
            }
        } catch (ItemNotFoundException ex) {
            System.out.println("Некорректное значение. Повторите ввод");
        }
    }


    /**
     * Вывод сообщения для пользователя
     *
     * @param message Выводимое сообщение
     */
    private void showMessage(String message) {
        switch (message) {
            case "emptyList":
                System.out.println("Журнал задач пуст. Повторите попытку позже.");
                System.out.println("\nНажмите Enter, чтобы продолжить... ");
                input.nextLine(); //отлавливаем энтер
                input.nextLine();
                break;
            case "incorrectInput":
                System.out.println("Некорректное значение. Повторите ввод.");
                System.out.println("\nНажмите Enter, чтобы продолжить... ");
                input.nextLine();
                input.nextLine();
        }
    }

    /**
     * "Очистка" консоли
     */
    private void consoleClear() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    /**
     * Метод оповещающий пользователя об удалении задачи
     *
     * @param task задача, удаленная из журнала задач
     */
    @Override
    public void taskDeleted(Task task) {
        System.out.println("\nЗадача " + task.getName() + " успешно удалена.");
    }

    /**
     * Метод оповещающий пользователя о добавлении задачи
     *
     * @param task Добавленная задача
     */
    @Override
    public void taskAdded(Task task) {
        System.out.println("\nЗадача " + task.getName() + " успешно добавлена.");
    }

    /**
     * Метод оповещающий пользователя об изменении задачи
     *
     * @param task Измененная задача
     */
    @Override
    public void taskEdited(Task task) {
        System.out.println("\nЗадача " + task.getName() + " успешно изменена.");
    }
}
