package taskmanager.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import taskmanager.TaskChangeSubscriber;
import taskmanager.exceptions.SubscriberNotFoundException;

import java.io.Serializable;
import java.time.LocalTime;


public class Task implements Serializable, Job {
    private String name;
    private String description;
    private LocalTime time;
    private String contacts;
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

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        StringBuffer task = new StringBuffer();
        task.append(name);
        task.append("      ");
        task.append(time);
        task.append("\n");
        task.append(description);
        task.append("\n");
        task.append(contacts);
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

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(this.toString());
    }
}
