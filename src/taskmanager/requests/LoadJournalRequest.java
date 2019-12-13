package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.util.LinkedList;
import java.util.List;

@JsonAutoDetect
public class LoadJournalRequest implements RequestInterface<List> {
    @JsonProperty("task")
    String message;
    @JsonDeserialize(as = LinkedList.class)
    private List<Task> data;

    public LoadJournalRequest(List<Task> data, String message) {
        this.message = message;
        this.data = data;
    }

    public LoadJournalRequest(String message) {
        this.message = message;
    }

    public LoadJournalRequest(){}

    @Override
    public List<Task> getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
