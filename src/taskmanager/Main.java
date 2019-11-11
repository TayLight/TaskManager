package taskmanager;

import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.sql.Time;

public class Main {

    public static void main(String[] args)
    {
        JournalTask journalTask = new JournalTask();

        Task task = new Task ("Тест", new Time(33));
        journalTask.addTask(task);
        System.out.println("TEST");
    }
}
