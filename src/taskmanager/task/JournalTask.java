package taskmanager.task;

import taskmanager.Manager;
import taskmanager.TaskChangeSubscriber;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.SubscriberNotFoundException;
import taskmanager.exceptions.TaskNotFoundException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;

public class JournalTask implements Manager, Serializable, TaskChangeSubscriber {
    /**
     * Список задач
     */
    private LinkedList<Task> tasks;

    private TaskChangeSubscriber subscriber;

    public LinkedList<Task> getList(){
        return tasks;
    }

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    public void addTask(Task newTask) throws NameTaskException {
        testTaskForName(newTask.getName());
        tasks.addLast(newTask);
        tasks.getLast().subscribe(this);
    }

    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setTime(newTime);
    }
    public void editTask(int index, String name) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setName(name);
    }
    public void editTaskDescription(int index, String description) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setDescription(description);
    }

    public void deleteTask(int index) throws TaskNotFoundException, SubscriberNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.remove(index);
    }

    private void testTaskForName(String name) throws NameTaskException {
        for (Task task : tasks)
            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем существует");
    }

    private void notifySubscriberChange(Task task)
    {
        subscriber.taskChanged(task);
    }

    private void notifySubscriberDeleted(Task task)
    {
        subscriber.taskDeleted(task);
    }

    @Override
    public void taskDeleted(Task task) {
        notifySubscriberDeleted(task);
    }

    @Override
    public void taskChanged(Task task) {
        notifySubscriberChange(task);
    }
}
