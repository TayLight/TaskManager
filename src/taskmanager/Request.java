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
public class Request {
    //@JsonProperty("task")
    /**
     * Команда на выполнение
     */
    private String request;
    /** Поле с пересылаемым журналом задач
     *
     */
    @JsonDeserialize(as = LinkedList.class)
    private LinkedList<Task> journal;

    public Request() {
    }

    /** Конструктор создания запроса-команды
     * @param request
     */
    public Request(String request) {
        this.request = request;
    }

    /** Конструктор создания запроса-команды с журналом задачи
     * @param request
     * @param journal
     */
    public Request(String request, LinkedList<Task> journal) {
        this.journal = journal;
    }

    /** Получение журнала задач
     * @return возвращает журнал задач
     */
    public LinkedList<Task> getJournal() {
        return journal;
    }

    /**Получение команды на выполение
     * @return возвращает команду
     */
    public String getRequest() {
        return request;
    }
}
