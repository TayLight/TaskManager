package taskmanager;

import taskmanager.task.JournalTask;
import taskmanager.task.Task;

public class Console {

    public static void main(String[] args) {
        Manager journalTask = new JournalTask<Task>();
        View view = new View(journalTask);
        Runnable run = new NotificationSystemThread(journalTask);
        Thread thread = new Thread(run);
        thread.setDaemon(true);
        thread.start();
        view.start();
    }
}
