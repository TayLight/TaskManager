package taskmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;
@JsonAutoDetect
public class NewTaskRequest implements Request<Task> {
    //@JsonProperty("task")
    /**
     * Команда на выполнение
     */
    private String message;
    /** Поле с пересылаемым журналом задач
     *
     */
    @JsonDeserialize(as = Task.class)
    private Task task;

    /** Конструктор создания запроса-команды с журналом задачи
     * @param request
     * @param task
     */
    public NewTaskRequest(String request, Task task) {
        this.task = task;
    }

    /** Получение задачи
     * @return возвращает задачу
     */
    public Task getData() {
        return task;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
