package taskmanager;

import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;

import java.sql.Time;

public interface Manager {

    public void addTask(Task newTask);
    public void deleteTask(int index) throws TaskNotFoundException;
    public void editTask(int numberTask, Time newTime) throws TaskNotFoundException;
    public void editTask(int numberTask, String text) throws TaskNotFoundException;
    public void editTask(int numberTask, String text, Time newTime) throws TaskNotFoundException;
}
