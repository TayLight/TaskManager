package taskmanager.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

public class EditTaskRequest extends RequestOld {

    //@JsonProperty("task")
    /**
     * Команда на выполнение
     */
    private String message;
    /**
     * Поле с пересылаемой задачей
     */
    @JsonDeserialize(as = Task.class)
    private Task data;

    /**
     * Конструктор создания запроса-команды с журналом задачи
     *
     * @param request
     * @param data
     */
    public EditTaskRequest(String request, Task data) {
        this.data = data;
    }

    public EditTaskRequest() {
    }

    /**
     * Получение задачи
     *
     * @return возвращает задачу
     */
    public Task getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
