package taskmanager;

import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.sql.Time;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args)
    {
        View view = new View();
        view.start();
    }
}
