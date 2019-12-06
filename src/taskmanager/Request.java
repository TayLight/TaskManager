package taskmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.util.LinkedList;

@JsonAutoDetect
public class Request {
    //@JsonProperty("task")
    private String request;
    @JsonDeserialize(as = LinkedList.class)
    private LinkedList<Task> journal;

    public Request() {
    }

    public Request(String request) {
        this.request = request;
    }

    public Request(String request, LinkedList<Task> journal) {
        this.journal = journal;
    }

    public LinkedList<Task> getJournal() {
        return journal;
    }

    public String getRequest() {
        return request;
    }
}
