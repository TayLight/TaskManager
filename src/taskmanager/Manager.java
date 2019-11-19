package taskmanager;

import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.SubscriberNotFoundException;
import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;


import java.time.LocalTime;

/**Интерфейс для взаимодействия с журналом задач
 */
public interface Manager {
    /**Метод добавления задачи в Журнал задач
     * @param newTask Новая задача
     */
    void addTask(Task newTask) throws NameTaskException;

    /** Метод удаления задачи, под индексом index
     * @param index Индекс удаляемой задачи
     * @throws TaskNotFoundException Задача не найдена
     */
    void deleteTask(int index) throws TaskNotFoundException, SubscriberNotFoundException;

    Task getTask(int index) throws TaskNotFoundException;
    /**Метод редактирования времени у задачи
     * @param index Индекс редактируемой задачи
     * @param newTime Новое устанавливаемое время
     * @throws TaskNotFoundException Задача не найдена
     */
    void editTask(int index, LocalTime newTime) throws TaskNotFoundException;

    /**Метод редактирования имени у задачи
     * @param index Индекс редактируемой задачи
     * @param text Новое имя у редактируемой задачи
     * @throws TaskNotFoundException Задача не найдена
     */
    void editTask(int index, String text) throws TaskNotFoundException;

    /**Метод редактирования описания у задачи
     * @param index Индекс редактируемой задачи
     * @param description Новое описание
     * @throws TaskNotFoundException Задача не найдена
     */
    void editTaskDescription(int index, String description) throws TaskNotFoundException;

}