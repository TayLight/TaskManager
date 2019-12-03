package taskmanager;

import taskmanager.task.Task;

import java.util.LinkedList;

public class Request  {
    private String request;
    private LinkedList<Task> journal;

    public Request(String request) {
        this.request = request;
    }

    public Request( String request,LinkedList<Task> journal) {
        this.journal = journal;
    }

    public LinkedList<Task> getJournal() {
        return journal;
    }

    public String getRequest() {
        return request;
    }
}
