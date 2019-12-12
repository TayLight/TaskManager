package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.awt.*;
import java.util.LinkedList;

@JsonAutoDetect
public class LoadJournalRequest implements RequestInterface<List> {
    @JsonProperty("task")
    String message;
    @JsonDeserialize(as = java.util.List.class)
    private List data;

    public LoadJournalRequest(String message, List data) {
        this.message = message;
        this.data = data;
    }

    public LoadJournalRequest(String message) {
        this.message = message;
    }

    @Override
    public List getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
