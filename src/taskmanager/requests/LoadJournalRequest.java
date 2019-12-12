package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.util.LinkedList;

@JsonAutoDetect
public class LoadJournalRequest implements RequestInterface<LinkedList<Task>> {
    @JsonProperty("task")
    String message;
    @JsonDeserialize(as = LinkedList.class)
    LinkedList<Task> data;

    public LoadJournalRequest(String message, LinkedList<Task> data) {
        this.message = message;
        this.data = data;
    }

    public LoadJournalRequest(String message) {
        this.message = message;
    }

    @Override
    public LinkedList<Task> getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
