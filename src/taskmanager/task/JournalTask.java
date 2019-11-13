package taskmanager.task;

import taskmanager.Manager;
import taskmanager.exceptions.TaskNotFoundException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;

public class JournalTask implements Manager, Serializable {
    private LinkedList<Task> tasks;

    public LinkedList<Task> getList(){
        return tasks;
    }

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    public void addTask(Task newTask) {
        tasks.addLast(newTask);
    }

    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
    }
    public void editTask(int index, String text) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setName(text);
    }
    public void editTask(int index, String text, LocalTime newTime) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
        tasks.get(index).setName(text);
    }

    public void deleteTask(int index) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.remove(index);
    }

}
