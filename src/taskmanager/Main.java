package taskmanager;

import taskmanager.conrollers.Manager;
import taskmanager.task.JournalTask;

public class Main {

    public static void main(String[] args) {
        Manager journalTask = new JournalTask();
        View view = new View(journalTask);
        Runnable run = new NotificationSystemThread(journalTask);
        Thread thread = new Thread(run);
        thread.setDaemon(true);
        thread.start();
        view.start();
    }
}
