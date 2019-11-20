package taskmanager;

import taskmanager.task.Task;

/**Интерфейс подписчика на изменения контроллера
 *
 */
public interface ControllerChangedSubscriber {

    /**Метод, оповещающий подписчиков об удалении задачи из журнала задач
     * @param task задача, удаленная из журнала задач
     */
    void taskDeleted(Task task);

    /**Метод, оповещающий об добавлении задачи в журнал задач
     * @param task Добавленная задача
     */
    void taskAdded(Task task);

    /** Метод, оповещающий об изменении задачи
     * @param task Измененная задача
     */
    void taskEdited(Task task);
}
