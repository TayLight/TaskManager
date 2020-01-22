package taskmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.util.LinkedList;

@JsonAutoDetect
public class JournalForSave {
    @JsonDeserialize(as = LinkedList.class)
    private LinkedList<Task> journalTask;

    public JournalForSave() {
    }

    public JournalForSave(LinkedList<Task> journalTask) {
        this.journalTask = journalTask;
    }

    public LinkedList getJournalTask() {
        return journalTask;
    }
}
