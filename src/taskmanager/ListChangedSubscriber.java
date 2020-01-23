package taskmanager;

public interface ListChangedSubscriber {

    /**
     * Метод, оповещающий подписчиков об удалении задачи из журнала задач
     */
    void listChanged();
}
