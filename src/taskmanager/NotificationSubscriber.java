package taskmanager;

import taskmanager.requests.Request;
import taskmanager.task.Task;

import java.util.List;

public interface NotificationSubscriber {

    /**
     * Метод, оповещающий подписчиков об удалении задачи из журнала задач
     *
     * @param index номер удаленной задачи
     */
    void taskDeleted(int index);

    /**
     * Метод, оповещающий об добавлении задачи в журнал задач
     *
     * @param task Добавленная задача
     */
    void taskAdded(Task task);

    /**
     * Метод, оповещающий об изменении задачи
     *
     * @param task Измененная задача
     */
    void taskUpdated(int index, Task task);

    void newJournalTask(List<Task> taskList);

    void notifyTask(int index);
}
