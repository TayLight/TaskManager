package taskmanager.task;
import taskmanager.exceptions.TaskNotFoundException;
import taskmanager.task.Task;

import java.io.Serializable;
import java.sql.Time;
import java.util.LinkedList;

public class JournalTask implements Serializable {
    private LinkedList<Task> tasks;

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
    }

    public void addTask(int index, Task newTask) {
        tasks.addLast(newTask);
    }

    public void editTask(int index, Time newTime) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
    }
    public void editTask(int index, String text)
    {
        tasks.get(index).setName(text);
    }
    public void editTask(int index, String text, Time newTime) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
        tasks.get(index).setName(text);
    }

    public void deleteTask(int index) throws TaskNotFoundException {
        if (tasks.size()<index)throw new TaskNotFoundException("Неверное значение индекса");
        tasks.remove(index);
    }
}
