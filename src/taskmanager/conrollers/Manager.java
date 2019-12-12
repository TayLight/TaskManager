package taskmanager.conrollers;

import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.Task;


import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

/**
 * Интерфейс для взаимодействия с журналом задач
 */
public interface Manager<T> {
    /**
     * Метод добавления задачи в Журнал задач
     *
     * @param newItem Новая задача
     */
    <T> void addItem(T newItem) throws NameTaskException;

    /**
     * Метод удаления задачи, под индексом index
     *
     * @param index Индекс удаляемой задачи
     * @throws ItemNotFoundException Задача не найдена
     */
    void deleteItem(int index) throws ItemNotFoundException;


    /**
     * Метод получения задачи по индексу
     * @param index индекс задачи в журнале задач
     * @return возвращает задачу с индексом index
     * @throws ItemNotFoundException Ошибка, задача не найден
     */
    Task getItem(int index) throws ItemNotFoundException;


    /**
     * Метод редактирования времени у задачи
     *
     * @param index   Индекс редактируемой задачи
     * @param newTime Новое устанавливаемое время
     * @throws ItemNotFoundException Задача не найдена
     */
    void editTask(int index, LocalTime newTime) throws ItemNotFoundException;

    /**
     * Метод редактирования имени у задачи
     *
     * @param index Индекс редактируемой задачи
     * @param text  Новое имя у редактируемой задачи
     * @throws ItemNotFoundException Задача не найдена
     */
    void editTask(int index, String text) throws ItemNotFoundException;

    /**
     * Метод редактирования описания у задачи
     * @param index       Индекс редактируемой задачи
     * @param description Новое описание
     * @throws ItemNotFoundException Задача не найдена
     */
    void editTaskDescription(int index, String description) throws ItemNotFoundException;

    /**
     * Метод, возвращающий размер журнала задача
     * @return Возвращает размером журнала задач
     */
    int size();

    void startWork() throws IOException;

    /**
     * Метод завершения работы менеджера
     */
    void finalWork();

    /** Метод загрузки журнала задач
     * @return возвращает полученный журнал задач
     */
    List<Task> getTasks();
    /**
     * Метод проверки на уникальность имени
     *
     * @param name Имя задачи для проверки
     * @throws NameTaskException Задача с таким именем уже есть
     */
    void checkUniqueName(String name) throws NameTaskException;


}