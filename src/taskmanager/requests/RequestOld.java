package taskmanager.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.util.LinkedList;
import java.util.List;

@JsonAutoDetect
public class RequestOld {
    //@JsonProperty("task")
    private String request;
    @JsonDeserialize(as = LinkedList.class)
    private List<Task> journal;

    public RequestOld() {
    }

    public RequestOld(String request) {
        this.request = request;
    }

    public RequestOld(String request, LinkedList<Task> journal) {
        this.journal = journal;
    }

    public List<Task> getJournal() {
        return journal;
    }

    public String getRequest() {
        return request;
    }
}
