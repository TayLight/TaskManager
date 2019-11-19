package taskmanager.task;

import taskmanager.ControllerChangedSubscriber;
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

    private ControllerChangedSubscriber subscriber;

    public LinkedList<Task> getList(){
        return tasks;
    }

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    public void addTask(Task newTask) throws NameTaskException {
        testTaskForName(newTask.getName());
        tasks.addLast(newTask);
        newTask.subscribe(this);
        subscriber.taskAdded(newTask);
    }

    public Task getTask(int index) throws TaskNotFoundException
    {
        if(index> tasks.size()) throw new TaskNotFoundException("Задача не найдена");
        return tasks.get(index);
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

    public void editTaskContacts(int index, String contacts) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        tasks.get(index).setContacts(contacts);
    }

    public void deleteTask(int index) throws TaskNotFoundException, SubscriberNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса");
        Task tempTask = tasks.get(index);
        tempTask.unSubscribe(this);
        tasks.remove(index);
        subscriber.taskDeleted(tempTask);
    }

    private void testTaskForName(String name) throws NameTaskException {
        for (Task task : tasks)
            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем существует");
    }

    private void notifySubscriberChange(Task task)
    {
        subscriber.taskEdited(task);
    }

    @Override
    public void taskChanged(Task task) {
        notifySubscriberChange(task);
    }
}
