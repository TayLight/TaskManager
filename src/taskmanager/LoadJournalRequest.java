package taskmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.util.LinkedList;

/**Класс для общения клиент-сервер
 *
 */
@JsonAutoDetect
public class LoadJournalRequest implements Request<LinkedList<Task>> {
    //@JsonProperty("task")
    /**
     * Команда на выполнение
     */
    private String message;
    /** Поле с пересылаемым журналом задач
     *
     */
    @JsonDeserialize(as = LinkedList.class)
    private LinkedList<Task> journal;

    public LoadJournalRequest() {
    }

    /** Конструктор создания запроса-команды
     * @param message
     */
    public LoadJournalRequest(String message) {
        this.message = message;
    }

    /** Конструктор создания запроса-команды с журналом задачи
     * @param request
     * @param journal
     */
    public LoadJournalRequest(String request, LinkedList<Task> journal) {
        this.journal = journal;
    }

    /** Получение журнала задач
     * @return возвращает журнал задач
     */
    public LinkedList<Task> getData() {
        return journal;
    }

    /**Получение команды на выполение
     * @return возвращает команду
     */
    public String getMessage() {
        return message;
    }
}
