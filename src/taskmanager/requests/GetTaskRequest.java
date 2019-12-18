package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

@JsonAutoDetect
public class GetTaskRequest implements RequestInterface<Task> {
    //@JsonProperty("task")
    private String message;

    @JsonDeserialize(as = Task.class)
    private Task data;

    public GetTaskRequest(String request, Task data) {
        this.data = data;
    }

    public GetTaskRequest() {
    }

    public Task getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

