package taskmanager.task;

import taskmanager.NotificationSystemThread;
import taskmanager.TaskChangeSubscriber;

import java.io.Serializable;
import java.time.LocalTime;

/** Задача
 *
 */
public class Task implements Serializable {
    /** Имя задачи
     *
     */
    private String name;
    /**Описание задачи
     *
     */
    private String description;
    /**
     * Время, во сколько задача оповестит пользователя
     */
    private LocalTime time;
    /**
     * Подписчик на обновление задачи
     */
    private TaskChangeSubscriber subscriber = null;
    /**
     * Система оповещения пользователя
     */
    private NotificationSystemThread notify = new NotificationSystemThread(this);

    /** Метод получения имени задачи
     * @return возвращает имя задачи
     */
    public String getName() {
        return name;
    }

    /** Метод изменения имени задачи
     * @param name Новое имя задачи
     */
    public void setName(String name) {
        this.name = name;
        taskEdited();
    }

    /** Метод изменения времени задачи
     * @param time Новое значение времени
     */
    public void setTime(LocalTime time) {
        this.time = time;
        taskEdited();
    }

    /**Метод получения времени задачи
     * @return Возвращает время выполнения задачи
     */
    public LocalTime getTime ()
    {
        return time;
    }

    /** Конструктор, создающий новый объект задачи
     * @param name Имя задачи
     * @param description Описание задачи
     * @param time Имя задачи
     */
    public Task(String name, String description, LocalTime time) {
        this.name = name;
        this.time = time;
        this.description = description;
        notify.start();
    }

    /** Метод получения системы оповещения
     * @return Возвращает систему оповещения задачи
     */
    public NotificationSystemThread getNotify() {
        return notify;
    }

    /**Метод получения описания задачи
     * @return Возвращает описание задачи
     */
    public String getDescription() {
        return description;
    }

    /**Метод изменяющий описание задачи
     * @param description новое описание задачи
     */
    void setDescription(String description) {
        this.description = description;
        taskEdited();
    }

    /**Метод возвращающий строковое представление задачи
     * @return Возвращает строковое представление задачи
     */
    @Override
    public String toString() {
        StringBuffer task = new StringBuffer();
        task.append(name);
        task.append("  [").append(time).append("]");
        task.append("\n");
        task.append(description);
        return task.toString();
    }

    /**Метод оповещающий подписчиков об изменении задачи
     *
     */
    public void taskEdited()
    {
        subscriber.taskChanged(this);
    }

    /**Метод для подписки на обновления модели
     * @param subscriber Новый подписчик
     */
    void subscribe(TaskChangeSubscriber subscriber)
    {
        this.subscriber = subscriber;
    }

    /**Метод отписки от обновлений модели
     *
     */
    void unsubscribe()
    {
        this.subscriber=null;
    }
}
