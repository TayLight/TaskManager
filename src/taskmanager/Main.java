package taskmanager;

import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.io.File;
import java.sql.Time;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args)
    {
        Manager journalTask = new JournalTask(); //д.б. манагер
        View view = new View(journalTask);
        Runnable run = new NotificationSystemThread(journalTask);
        Thread thread = new Thread(run);
        thread.setDaemon(true);
        thread.start();
        view.start();
    }
}
