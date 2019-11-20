package taskmanager.task;

import taskmanager.ControllerChangedSubscriber;
import taskmanager.Manager;
import taskmanager.NotificationSystemThread;
import taskmanager.TaskChangeSubscriber;
import taskmanager.exceptions.NameTaskException;
import taskmanager.exceptions.TaskNotFoundException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;

public class JournalTask implements Manager, Serializable, TaskChangeSubscriber {
    /**
     * Список задач
     */
    private LinkedList<Task> tasks;
    private ControllerChangedSubscriber subscriber = null;

    public JournalTask() {
        tasks = new LinkedList<>();
    }

    public Task getTask (int index){
        return tasks.get(index);
    }

    public int size (){
        return tasks.size();
    }

    public void addTask(Task newTask){
        tasks.addLast(newTask);
        newTask.subscribe(this);
        taskAdded(newTask);
        NotificationSystemThread newNotify = new NotificationSystemThread(newTask);
        newNotify.start();
    }

    public void editTask(int index, LocalTime newTime) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.get(index).setTime(newTime);
        taskChanged(tasks.get(index));
    }
    public void editTask(int index, String name) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.get(index).setName(name);
        taskChanged(tasks.get(index));
    }
    public void editTaskDescription(int index, String description) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.get(index).setDescription(description);
        taskChanged(tasks.get(index));
    }

    public void deleteTask(int index) throws TaskNotFoundException {
        if (tasks.size()<index) throw new TaskNotFoundException("Неверное значение индекса.");
        tasks.get(index).unsubscribe();
        Task tempTask = tasks.get(index);
        tasks.remove(index);
        taskDeleted(tempTask);
    }

    public void testTaskForName(String name) throws NameTaskException {
        for (Task task : tasks) {
            if (task.getName().equals(name)) throw new NameTaskException("Задача с таким именем уже существует.");
        }
    }

    public void testTaskForIndex(int index) throws TaskNotFoundException{
        if (tasks.size() < index) throw new TaskNotFoundException("Неверное значение индекса.");
    }

    @Override
    public void taskChanged(Task task) {
        subscriber.taskEdited(task);
    }

    private void taskAdded(Task task)
    {
        subscriber.taskAdded(task);
    }

    private void taskDeleted(Task task)
    {
        subscriber.taskDeleted(task);
    }

    public void subscribe(ControllerChangedSubscriber subscriber)  {
        this.subscriber = subscriber;
    }

    public void unsubscribe()
    {
        this.subscriber=null;
    }
}
