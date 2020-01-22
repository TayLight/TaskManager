package taskmanager.task;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Задача
 */
public class Task implements Serializable {
    /**
     * Имя задачи
     */
    private String name;
    /**
     * Описание задачи
     */
    private String description;
    /**
     * Время, во сколько задача оповестит пользователя
     */
    private LocalTime time;

    /**
     * Флаг актуальности задачи. Задача актуальна, пока она не выполнилась
     */
    private boolean relevance;

    /**
     * идентификатор задачи
     */
    private int id;

    /**
     * Метод получения имени задачи
     *
     * @return возвращает имя задачи
     */
    public String getName() {
        return name;
    }

    /**
     * Метод изменения имени задачи
     *
     * @param name Новое имя задачи
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод изменения времени задачи
     *
     * @param time Новое значение времени
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Метод получения времени задачи
     *
     * @return Возвращает время выполнения задачи
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Конструктор, создающий новый объект задачи
     *
     * @param name        Имя задачи
     * @param description Описание задачи
     * @param time        Имя задачи
     */
    public Task(String name, String description, LocalTime time) {
        this.name = name;
        this.time = time;
        this.description = description;
        relevance = true;
        id = time.getHour()*31+time.getMinute()*(-31);
    }

    public Task(String name, String description, LocalTime time, boolean relevance) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.relevance=relevance;
        id = time.getHour()*31+time.getMinute()*(-31);
    }

    public Task() {
    }

    /**
     * Метод получения описания задачи
     *
     * @return Возвращает описание задачи
     */
    public String getDescription() {
        return description;
    }

    /**
     * Метод изменяющий описание задачи
     *
     * @param description новое описание задачи
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Метод возвращающий строковое представление задачи
     *
     * @return Возвращает строковое представление задачи
     */
    @Override
    public String toString() {
        StringBuffer task = new StringBuffer();
        task.append(name);
        task.append("  [").append(time).append("]");
        if (!relevance) {
            task.append("  [✔] ");
        }
        task.append("\n");
        task.append(description);
        return task.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Task task = (Task) super.clone();
        return task;
    }

    /**
     * Установка актуальности задачи
     *
     * @param relevance Статус актуальности задачи
     */
    public void setRelevance(boolean relevance) {
        this.relevance = relevance;
    }

    /**
     * Получение значения актуальности
     *
     * @return Значение актуальности задачи
     */
    public boolean getRelevance() {
        return relevance;
    }

    /**
     * @return озвращает ID задачи
     */
    public int getId() {
        return id;
    }
}
