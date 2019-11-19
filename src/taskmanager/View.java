package taskmanager;
import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class View implements ControllerChangedSubscriber {
    /**
     * Сканер ввода
     */
    static Scanner input = new Scanner(System.in);
    /**
     * Журнал задач
     */
    static JournalTask journalTask = new JournalTask();
    /**
     * Маркер выход
     */
    static boolean exit = false;

    public static void start() {
        while (!exit) {
            for(int i = 0; i < 40; i++) System.out.println(); // "очистка" консоли
            System.out.println("[TASK MANAGER]\n");
            menu();
            System.out.println("\nВыберите пункт меню:");
            try{
                int menuChoice = input.nextInt();
                action(menuChoice);
            }
            catch(InputMismatchException ex){
                System.out.println("Некорректное значение. Повторите ввод.\n\nНажмите Enter, чтобы продолжить...");
                input.nextLine();
                input.nextLine();
            }
            catch (ArrayIndexOutOfBoundsException ex){
                System.out.println("Некорректное значение. Повторите ввод");
                System.out.println("\nНажмите Enter, чтобы продолжить...");
                input.nextLine(); //отлавливаем энтер
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            ;
        }
        System.out.println("Завершение работы программы...");

    }

    public static void menu(){
        System.out.println("Меню:" +
                "\n[1] Добавить задачу" +
                "\n[2] Редактировать задачу" +
                "\n[3] Удалить задачу" +
                "\n[4] Список задач" +
                "\n[0] Выход");
    }

    public static void action(int menuChoice) {
        switch (menuChoice){
            case 1: //добавить задачу
                for(int i = 0; i < 40; i++) System.out.println();
                System.out.print("[Добавление задачи]" +
                        "\n\nНазвание: ");
                input.nextLine(); //отлавливаем энтер
                String name = input.nextLine();
                System.out.print("\nОписание: ");
                String description = input.nextLine();
                System.out.print("\nВремя (ЧЧ ММ СС): ");
                String strTime = input.nextLine();
                String[] splitTime = strTime.split(" ");
                int hour = Integer.parseInt(splitTime[0]);
                int minute = Integer.parseInt(splitTime[1]);
                int second = Integer.parseInt(splitTime[2]);
                LocalTime time;
                try{
                    time = LocalTime.of(hour, minute, second);
                }
                catch (DateTimeException ex){
                    System.out.println("Некорректное время. Повторите ввод");
                    System.out.println("\nНажмите Enter, чтобы продолжить...");
                    input.nextLine(); //отлавливаем энтер
                    break;
                }
                Task task = new Task (name, description , time);
                journalTask.addTask(task);
                System.out.println("\nЗадача успешно добавлена в журнал.\nНажмите Enter, чтобы продолжить...");
                input.nextLine();
                break;
            case 2: //редактировать задачу
                if (journalTask.getList().size() == 0){
                    System.out.println("Журнал задач пуст. Повторите попытку позже.");
                    System.out.println("\nНажмите Enter, чтобы продолжить...");
                    input.nextLine(); //отлавливаем энтер
                    input.nextLine();
                }
                else{
                    for(int i = 0; i < 40; i++) System.out.println();
                    System.out.println("[Редактирование задачи]");
                    //редактирование
                }
                break;
            case 3: //удалить задачу
                if (journalTask.getList().size() == 0) {
                    System.out.println("Журнал задач пуст. Повторите попытку позже.");
                    System.out.println("\nНажмите Enter, чтобы продолжить...");
                    input.nextLine(); //отлавливаем энтер
                    input.nextLine();
                }
                else {
                    for (int i = 0; i < 40; i++) System.out.println();
                    System.out.println("[Удаление задачи]\n\n");
                    showTaskList();
                    System.out.println("Индекс: ");
                    int index = input.nextInt();
                    try {
                        String taskName = journalTask.getList().get(index).getName();
                        journalTask.deleteTask(index);
                        System.out.println("\nЗадача '" + taskName + "' успешно удалена из журнала.");
                    } catch (TaskNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    } catch (IndexOutOfBoundsException ex){
                        System.out.println("Неверное значение индекса");
                    }
                    System.out.println("\nНажмите Enter, чтобы продолжить...");
                    input.nextLine(); //отлавливаем энтер
                    input.nextLine();
                }
                break;
            case 4: //вывести список задач
                if (journalTask.getList().size() == 0) {
                    System.out.println("Журнал задач пуст. Повторите попытку позже.");
                    System.out.println("\nНажмите Enter, чтобы продолжить...");
                    input.nextLine(); //отлавливаем энтер
                    input.nextLine();
                }
                else{
                    for (int i = 0; i < 40; i++) System.out.println();
                    System.out.println("[Список задач]\n");
                    showTaskList();
                    System.out.println("\nНажмите Enter, чтобы вернуться в главное меню...");
                    input.nextLine(); //отлавливаем энтер
                    input.nextLine();
                }
                break;
            case 0: //выход
                exit = true;
                break;
            default:
                System.out.println("Некорректный пункт меню. Повторите ввод.\n\nНажмите Enter, чтобы продолжить...");
                input.nextLine(); //отлавливаем энтер
                input.nextLine();
                break;
        }
    }

    /**
     *
     */
    public static void showTaskList(){
        if (journalTask.getList().size() == 0) {
            System.out.println("Журнал задач пуст.\n");
            return;
        }
        for(int i = 0; i < journalTask.getList().size(); i++){
            System.out.println(i + ". " + journalTask.getList().get(i).getName());
            System.out.println(journalTask.getList().get(i).getDescription());
            System.out.println(journalTask.getList().get(i).getTime() + "\n");
        }
    }
}
