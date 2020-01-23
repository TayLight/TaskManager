package taskmanager;

import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;

import java.io.IOException;
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
    void addItem(T newItem) throws NameTaskException, IOException;

    /**
     * Метод удаления задачи, под индексом index
     *
     * @param index Индекс удаляемой задачи
     * @throws ItemNotFoundException Задача не найдена
     */
    void deleteItem(int index) throws ItemNotFoundException, IOException;


    /**
     * Метод получения задачи по индексу
     *
     * @param index индекс задачи в журнале задач
     * @return возвращает задачу с индексом index
     * @throws ItemNotFoundException Ошибка, задача не найден
     */
    T getItem(int index) throws ItemNotFoundException;

    /**
     * Метод обновления свойств задачи
     *
     * @param index индек изменяемой задачи
     * @param item  задача
     * @throws ItemNotFoundException задача не найдена
     */
    void updateItem(int index, T item) throws ItemNotFoundException, IOException;

    /**
     * Метод, возвращающий размер журнала задача
     *
     * @return Возвращает размером журнала задач
     */
    int getSize();

    /**
     * метод начала работы
     * @throws IOException
     */
    void startWork() throws IOException;

    /**
     * Метод завершения работы менеджера
     */
    void finalWork() throws IOException;

    /**
     * Метод загрузки журнала задач
     *
     * @return возвращает полученный журнал задач
     */
    List<T> getItems() throws IOException;

    /**
     * Метод проверки на уникальность имени
     *
     * @param name Имя задачи для проверки
     * @throws NameTaskException Задача с таким именем уже есть
     */
    void checkUniqueName(String name) throws NameTaskException, IOException;

    /** Метод подписки на обновления
     * @param subscriber Новый подписчик
     */
    void subscribe(ListChangedSubscriber subscriber);

    /**Метод отписки от обновлений
     */
    void unsubscribe();


}