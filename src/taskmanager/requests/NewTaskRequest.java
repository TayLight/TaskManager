package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;
@JsonAutoDetect
public class NewTaskRequest implements RequestInterface<Task> {
    //@JsonProperty("task")
    /**
     * Команда на выполнение
     */
    private String message;
    /** Поле с пересылаемой задачей
     *
     */
    @JsonDeserialize(as = Task.class)
    private Task data;

    /** Конструктор создания запроса-команды с журналом задачи
     * @param request
     * @param data
     */
    public NewTaskRequest(String request, Task data) {
        this.data = data;
    }

    public NewTaskRequest(){}

    /** Получение задачи
     * @return возвращает задачу
     */
    public Task getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
