package taskmanager;

import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;


import java.time.LocalTime;

public interface Manager {
    public void addTask(Task newTask);
    public void deleteTask(int index) throws TaskNotFoundException;
    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException;
    public void editTask(int index, String text) throws TaskNotFoundException;
    public void editTask(int index, String text, LocalTime newTime) throws TaskNotFoundException;

}