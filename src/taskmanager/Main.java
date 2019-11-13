package taskmanager;

import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.sql.Time;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args)
    {
        Task newTask = new Task("Name", "dsa", LocalTime.of(2,4,4));
        System.out.println(newTask.toString());
        View.start();
    }
}
