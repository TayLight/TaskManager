package taskmanager;

import taskmanager.task.Task;

/**
 * Интерфес подписчика на изменения задачи
 */
public interface TaskChangeSubscriber {

    /**Метод оповещения подписчиков об изменении задачи
     * @param task Измененная задача
     */
    void taskChanged(Task task);
}
