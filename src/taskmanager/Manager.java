package taskmanager;

import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;


import java.time.LocalTime;

public interface Manager {
    /**
     * @param newTask Новая задача
     *                Метод добавления задачи в Журнал задач
     */
    public void addTask(Task newTask);

    /**
     * @param index Индекс удаляемой задачи
     * @throws TaskNotFoundException
     * Метод удаления задачи, под индексом index
     */
    public void deleteTask(int index) throws TaskNotFoundException;

    /**
     * @param index Индекс редактируемой задачи
     * @param newTime Новое устанавливаемое время
     * @throws TaskNotFoundException
     * Метод редактирования времени у задачи
     */
    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException;

    /**
     * @param index Индекс редактируемой задачи
     * @param text Новое имя у редактируемой задачи
     * @throws TaskNotFoundException
     * Метод редактирования имени у задачи
     */
    public void editTask(int index, String text) throws TaskNotFoundException;

    /**
     * @param index Индекс редактируемой задачи
     * @param text  Новое имя у задачи
     * @param newTime Новое время у задачи
     * @throws TaskNotFoundException
     * Метод редактирования имени и времени у задачи
     */
    public void editTask(int index, String text, LocalTime newTime) throws TaskNotFoundException;

}