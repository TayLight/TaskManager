package taskmanager;

import java.util.LinkedList;

public class JournalForSave<T> {

    private LinkedList<T> journalTask;

    public JournalForSave(LinkedList<T> journalTask) {
        this.journalTask = journalTask;
    }

    public LinkedList<T> getJournalTask() {
        return journalTask;
    }
}
