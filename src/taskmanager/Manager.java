package taskmanager;

import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;

import java.sql.Time;

/**
 * Интерфейс, для связи UI с внтуренней реализацией
 */
public interface Manager {

    /**
     * @param newTask новая задача, добавляемая в список
     * Метод для добавления задачи
     */
    public void addTask(Task newTask);

    /**
     * @param index индекс удаляемой задачи
     * @throws TaskNotFoundException выход индекса за размер журнала задач
     * Метод для удаления задачи
     */
    public void deleteTask(int index) throws TaskNotFoundException;

    /**
     * @param numberTask индекс задачи
     * @param newTime новое устанавливаемое время
     * @throws TaskNotFoundException выход индекса за размер журнала задач
     * Метод для редактирования времени задачи
     */
    public void editTask(int numberTask, Time newTime) throws TaskNotFoundException;

    /**
     * @param numberTask индекс редактируемой задачи
     * @param text новое название задачи
     * @throws TaskNotFoundException выход за размер журнала задач
     * Метод для редактирования названия задачи
     */
    public void editTask(int numberTask, String text) throws TaskNotFoundException;

    /**
     * @param numberTask индекс редактирумой задачи
     * @param text новое название задачи
     * @param newTime новое значение времени
     * @throws TaskNotFoundException выход за размер журнала задач
     * Метод для редактирования времени и названия задачи
     */
    public void editTask(int numberTask, String text, Time newTime) throws TaskNotFoundException;
}