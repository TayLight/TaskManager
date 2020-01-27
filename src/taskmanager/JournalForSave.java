package taskmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import taskmanager.task.Task;

import java.util.LinkedList;
import java.util.List;


public class JournalForSave {
    private List<Task> journalTask;

    public JournalForSave() {
    }

    public JournalForSave(List<Task> journalTask) {
        this.journalTask = journalTask;
    }

    public List<Task> getJournalTask() {
        return journalTask;
    }
}
