package taskmanager.task;
import taskmanager.Manager;
import taskmanager.exceptions.TaskNotFoundException;

import java.io.Serializable;
import java.sql.Time;
import java.util.LinkedList;

/**
 * Класс, содержащий список задач и методы для работы с ним
 */
public class JournalTask implements Serializable, Manager {
    /**
     * Содержит список задач
     */
    private LinkedList<Task> tasks;

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    /**
     * Добавляет новую задачу в конец JournalTask
     */
    public void addTask(Task newTask) {
        tasks.addLast(newTask);
    }

    /**
     * @param index номер редактируемого элемента
     * @param newTime Новое задаваемое время
     * @throws TaskNotFoundException Выход индекса за размер списка
     * Метод для редактирования времени у модели
     */
    public void editTask(int index, Time newTime) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
    }

    /**
     * @param index номер редактируемого элемента
     * @param text новое название
     * @throws TaskNotFoundException Выход индекса задачи за размер списка
     * Метод для редактирования названия задачи
     */
    public void editTask(int index, String text) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setName(text);
    }

    /**
     * @param index номер редактируемоей задачи
     * @param text  новое название алачи
     * @param newTime новое время выполнения задачи
     * @throws TaskNotFoundException выход за границы размера списка задач
     * Метод для редактирования задачи и времени выполнения
     */
    public void editTask(int index, String text, Time newTime) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
        tasks.get(index).setName(text);
    }

    /**
     * @param index номер удаляемой задачи
     * @throws TaskNotFoundException выход за границы размера задач
     * Метод удаления задачи
     */
    public void deleteTask(int index) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.remove(index);
    }
}
