package taskmanager;

import taskmanager.task.Task;

import java.sql.Time;

public interface Manager {

    public void addTask(Task newTask);
    public void editTask(int numberTask, Time newTime);
    public void editTask(int numberTask, String text);
    public void editTask(int numberTask, String text, Time newTime);
}