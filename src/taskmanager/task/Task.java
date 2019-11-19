package taskmanager.task;

import taskmanager.TaskChangeSubscriber;
import taskmanager.exceptions.SubscriberNotFoundException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class Task implements Serializable {
    private String name;
    private String description;
    private LocalTime time;
    private TaskChangeSubscriber subscriber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        taskEdited();
    }

    public void setTime(LocalTime time) {
        this.time = time;
        taskEdited();
    }

    public LocalTime getTime ()
    {
        return time;
    }

    public Task(String name, LocalTime time) {
        this.name = name;
        this.time = time;
    }

    public Task(String name, String description, LocalTime time) {
        this.name = name;
        this.time = time;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        taskEdited();
    }

    @Override
    public String toString() {
        StringBuffer task = new StringBuffer();
        task.append(name);
        task.append("      ");
        task.append(time);
        task.append("\n");
        task.append(description);
        return task.toString();
    }

    public void subscribe(TaskChangeSubscriber subscriber)
    {
        this.subscriber = subscriber;
    }

    public void unSubscribe(TaskChangeSubscriber subscriber) throws SubscriberNotFoundException {
        this.subscriber=null;
    }

    public void taskEdited()
    {
        if (this.subscriber != null)
        {
            subscriber.taskChanged(this);
        }
    }
}
