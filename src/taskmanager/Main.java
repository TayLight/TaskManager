package taskmanager;

import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.sql.Time;

public class Main {

    public static void main(String[] args) throws TaskNotFoundException {
        JournalTask journalTask = new JournalTask();

        Task task = new Task ("Тест", new Time(33));
        journalTask.addTask(task);
        System.out.println("TEST");
        System.out.println(journalTask.toString());
        journalTask.deleteTask(0);
        System.out.println(journalTask.toString());
    }
}
